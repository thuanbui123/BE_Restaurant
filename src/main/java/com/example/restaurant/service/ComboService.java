package com.example.restaurant.service;

import com.example.restaurant.entity.ComboEntity;
import com.example.restaurant.mapper.ComboMapper;
import com.example.restaurant.repository.ComboRepository;
import com.example.restaurant.request.ComboRequest;
import com.example.restaurant.utils.PaginateUtil;
import com.example.restaurant.utils.Slugify;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ComboService {
    @Autowired
    private ComboRepository repository;
    /*
    * Lập lịch hàng ngày vào lúc 0:00
    * Chỉ chạy khi ứng dụng run
    * Sau deploy lên server thì hàm sẽ được chạy vào lúc 0:00 hàng ngày và khi server đang chạy
    */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateComboStatuses() {
        repository.activateCombos();
        repository.deactivateExpiredCombos();
    }

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                ComboMapper::mapToResponse);
    }

    public ResponseEntity<?> findAllUser (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                ComboMapper::mapToUserResponse);
    }

    public ResponseEntity<?> findBySlug (String slug, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findBySlugContainingIgnoreCase(slug, pageable),
                pageable,
                ComboMapper::mapToResponse
        );
    }

    public boolean existsById (Integer id) {
        return repository.existsById(id);
    }

    public ComboEntity findOneById (Integer id) {
        return repository.findOneById(id);
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query) {
        Pageable pageable =  PageRequest.of(page, size);
        if (prefix.equals("find-all") && query == null) {
            return findAll(pageable);
        } else if (prefix.equals("search") && query != null) {
            final String slug = Slugify.toSlug(query);
            return findBySlug(slug, pageable);
        } else if (prefix.equals("find-all-user") && query == null) {
            return findAllUser(pageable);
        }
        return new ResponseEntity<>("API không tồn tại!", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> addData (ComboRequest request) {
        try {
            if(repository.existsByCode(request.getCode())) {
                return ResponseEntity.badRequest().body("Mã combo thức ăn đã tồn tại!");
            }
            ComboEntity entity = ComboMapper.mapToEntity(request);
            repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm combo thức ăn mới thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi thêm combo thức ăn mới: "+ e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateData (String code, ComboRequest request) {
        try {
            String slug = Slugify.toSlug(request.getName());
            ComboEntity existsEntity = repository.findOneByCode(code);
            if (existsEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combo thức ăn không tồn tại!");
            }
            if (existsEntity.getStatus().equals("Đã hết hạn") || existsEntity.getStatus().equals("Đang áp dụng")) {
                return ResponseEntity.badRequest().body("Không được thay đổi khi combo món ăn đã hết hạn hoặc đang áp dụng!");
            }
            existsEntity.setName(request.getName());
            existsEntity.setImg(request.getImg());
            existsEntity.setDescription(request.getDescription());
            existsEntity.setSlug(slug);
            repository.save(existsEntity);
            return ResponseEntity.ok().body("Cập nhật combo thức ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật combo thức ăn: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (String code) {
        try {
            ComboEntity entity = repository.findOneByCode(code);
            if (entity == null) {
                return new ResponseEntity<>("Combo thức ăn không tồn tại!", HttpStatus.NOT_FOUND);
            }
            if (entity.getStatus().equals("Đã hết hạn") || entity.getStatus().equals("Đang áp dụng")) {
                return ResponseEntity.badRequest().body("Không được xóa khi combo món ăn đã hết hạn hoặc đang áp dụng!");
            }
            repository.deleteByCode(code);
            return ResponseEntity.ok().body("Xóa combo thức ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi xóa combo thức ăn: " + e.getMessage());
        }
    }
}
