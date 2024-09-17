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
import org.springframework.stereotype.Service;

@Service
public class ComboService {
    @Autowired
    private ComboRepository repository;

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                ComboMapper::mapToResponse);
    }

    public ResponseEntity<?> findBySlug (String slug, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findBySlugContainingIgnoreCase(slug, pageable),
                pageable,
                ComboMapper::mapToResponse
        );
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query) {
        Pageable pageable =  PageRequest.of(page, size);
        if (prefix.equals("find-all") && query == null) {
            return new ResponseEntity<>(findAll(pageable), HttpStatus.OK);
        } else if (prefix.equals("search") && query != null) {
            final String slug = Slugify.toSlug(query);
            return new ResponseEntity<>(findBySlug(slug, pageable), HttpStatus.OK);
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
            existsEntity.setName(request.getName());
            existsEntity.setImg(request.getImg());
            existsEntity.setPrice(request.getPrice());
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
            repository.deleteByCode(code);
            return ResponseEntity.ok().body("Xóa combo thức ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi xóa combo thức ăn: " + e.getMessage());
        }
    }
}
