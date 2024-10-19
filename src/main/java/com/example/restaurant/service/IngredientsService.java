package com.example.restaurant.service;

import com.example.restaurant.entity.IngredientsEntity;
import com.example.restaurant.entity.SuppliersEntity;
import com.example.restaurant.mapper.IngredientsMapper;
import com.example.restaurant.repository.IngredientsRepository;
import com.example.restaurant.request.IngredientsRequest;
import com.example.restaurant.utils.PaginateUtil;
import com.example.restaurant.utils.Slugify;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class IngredientsService {
    @Autowired
    private IngredientsRepository repository;

    @Autowired
    private SupplierService supplierService;

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                IngredientsMapper::mapToAdminResponse);
    }

    public ResponseEntity<?> findBySlug (String slug) {
        return ResponseEntity.ok().body(repository.findBySlugContainingIgnoreCase(slug));
    }

    public IngredientsEntity findById (Integer id) {
        return repository.findOneById(id);
    }

    public boolean existsById (Integer id) {
        return repository.existsById(id);
    }

    public ResponseEntity<?> findOneByCode(String code) {
        return ResponseEntity.ok().body(IngredientsMapper.mapToAdminResponse(repository.findOneByCode(code)));
    }
    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query) {

        if (prefix.equals("find-all") && query == null) {
            Pageable pageable = PageRequest.of(page, size);
            return new ResponseEntity<>(findAll(pageable), HttpStatus.OK);
        } else if (prefix.equals("search") && query != null) {
            final String slug = Slugify.toSlug(query);
            return findBySlug(slug);
        } else if (prefix.equals("find-one-by-code") && query != null) {
            return findOneByCode(query);
        }
        return new ResponseEntity<>("API không tồn tại!", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> addData (IngredientsRequest request) {
        try {
            if(repository.existsByCode(request.getCode())) {
                return ResponseEntity.badRequest().body("Mã nguyên liệu đã tồn tại!");
            }
            IngredientsEntity entity = IngredientsMapper.mapToEntity(request);
            if (entity != null) {
                repository.save(entity);
                return ResponseEntity.status(HttpStatus.CREATED).body("Thêm nguyên liệu mới thành công.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tồn tại nhà cung cấp!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi thêm nguyên liệu mới: "+ e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateData (String code, IngredientsRequest request) {
        try {
            IngredientsEntity existsEntity = repository.findOneByCode(code);
            if (existsEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nguyên liệu không tồn tại!");
            }
            existsEntity.setName(request.getName());
            existsEntity.setSlug(Slugify.toSlug(request.getName()));
            existsEntity.setImg(request.getImg());
            existsEntity.setQuantity(request.getQuantity());
            existsEntity.setType(request.getType());
            existsEntity.setUnit(request.getUnit());
            repository.save(existsEntity);
            return ResponseEntity.ok().body("Cập nhật thông tin nguyên liệu thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật thông tin nguyên liệu: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (String code) {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nguyên liệu không tồn tại!");
            }
            repository.deleteByCode(code);
            return ResponseEntity.ok().body("Xóa thông tin nguyên liệu thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi xóa thông tin nguyên liệu: " + e.getMessage());
        }
    }
}
