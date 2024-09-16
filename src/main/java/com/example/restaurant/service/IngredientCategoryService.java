package com.example.restaurant.service;

import com.example.restaurant.entity.IngredientCategoryEntity;
import com.example.restaurant.mapper.IngredientCategoryMapper;
import com.example.restaurant.repository.IngredientCategoryRepository;
import com.example.restaurant.request.IngredientCategoryRequest;
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
public class IngredientCategoryService {
    @Autowired
    private IngredientCategoryRepository repository;

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                IngredientCategoryMapper::mapToAdminResponse
        );
    }

    public ResponseEntity<?> findBySlug (String slug, Pageable pageable) {
        return  PaginateUtil.paginate(
                (pg) -> repository.findBySlugContainingIgnoreCase(slug, pageable),
                pageable,
                IngredientCategoryMapper::mapToAdminResponse
        );
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page, size);;
        if (prefix.equals("find-all") && query == null) {
            return new ResponseEntity<>(findAll(pageable), HttpStatus.OK);
        } else if (prefix.equals("search") && query != null) {
            final String slug = Slugify.toSlug(query);
            return new ResponseEntity<>(findBySlug(slug, pageable), HttpStatus.OK);
        }
        return new ResponseEntity<>("API không tồn tại!", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> addData (IngredientCategoryRequest request) {
        try {
            if (repository.existsBySlug(Slugify.toSlug(request.getName()))) {
                return ResponseEntity.badRequest().body("Danh mục nguyên liệu đã tồn tại!");
            }
            IngredientCategoryEntity entity = IngredientCategoryMapper.mapToEntity(request);
            repository.save(entity);
            return ResponseEntity.ok().body("Thêm danh mục nguyên liệu thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi thêm danh mục nguyên liệu mới: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateData (Integer id, IngredientCategoryRequest request) {
        try {
            if (!repository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Danh mục nguyên liệu không tồn tại!");
            }
            IngredientCategoryEntity existsEntity = repository.findOneById(id);
            existsEntity.setName(request.getName());
            existsEntity.setSlug(Slugify.toSlug(request.getName()));
            existsEntity.setDescription(request.getDescription());
            repository.save(existsEntity);
            return ResponseEntity.ok().body("Cập nhật danh mục nguyên liệu thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật danh mục nguyên liệu: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (Integer id) {
        try {
            IngredientCategoryEntity entity = repository.findOneById(id);
            if (entity == null) {
                return new ResponseEntity<>("Danh mục nguyên liệu không tồn tại!", HttpStatus.NOT_FOUND);
            }
            repository.deleteById(id);
            return ResponseEntity.ok().body("Xóa danh mục nguyên liệu thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi xóa danh mục nguyên liệu: " + e.getMessage());
        }
    }
}
