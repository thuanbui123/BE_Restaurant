package com.example.restaurant.service;

import com.example.restaurant.entity.FoodCategoryEntity;
import com.example.restaurant.mapper.FoodCategoryMapper;
import com.example.restaurant.repository.FoodCategoryRepository;
import com.example.restaurant.request.FoodCategoryRequest;
import com.example.restaurant.response.FoodCategoryUserResponse;
import com.example.restaurant.utils.PaginateUtil;
import com.example.restaurant.utils.Slugify;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FoodCategoryService {
    @Autowired
    private FoodCategoryRepository repository;

    public List<FoodCategoryUserResponse> findAll () {
        List<FoodCategoryUserResponse> userResponses = new ArrayList<>();
        List<FoodCategoryEntity> entities = repository.findAll();
        if (!entities.isEmpty()) {
            for (FoodCategoryEntity entity : entities) {
                userResponses.add(FoodCategoryMapper.mapToUserResponse(entity));
            }
            return userResponses;
        }
        return null;
    }

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                FoodCategoryMapper::mapToAdminResponse
        );
    }

    public ResponseEntity<?> findBySlug (String slug, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findBySlugContainingIgnoreCase(slug, pageable),
                pageable,
                FoodCategoryMapper::mapToAdminResponse
        );
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query) {
        Pageable pageable = null;
        if (page != null && size != null) {
            pageable = PageRequest.of(page, size);
        }
        if (prefix.equals("user-find-all") && query == null) {
            return ResponseEntity.ok(findAll());
        } else if (prefix.equals("admin-find-all") && query == null) {
            return new ResponseEntity<>(findAll(pageable), HttpStatus.OK);
        } else if (prefix.equals("search") && query != null) {
            final String slug = Slugify.toSlug(query);
            return new ResponseEntity<>(findBySlug(slug, pageable), HttpStatus.OK);
        }
        return new ResponseEntity<>("API không tồn tại!", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> addData (FoodCategoryRequest request) {
        try {
            if(repository.findByName(request.getName()).isPresent()) {
                return ResponseEntity.badRequest().body("Tên danh mục thức ăn đã tồn tại!");
            }
            FoodCategoryEntity entity = FoodCategoryMapper.mapToEntity(request);
            repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm danh mục thức ăn mới thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi thêm danh mục thức ăn mới: "+ e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateData (Integer id, FoodCategoryRequest request) {
        try {
            String slug = Slugify.toSlug(request.getName());
            FoodCategoryEntity existsEntity = repository.findOneById(id);
            if (existsEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Danh mục thức ăn không tồn tại!");
            }
            existsEntity.setName(request.getName());
            existsEntity.setDescription(request.getDescription());
            existsEntity.setSlug(slug);
            repository.save(existsEntity);
            return ResponseEntity.ok().body("Cập nhật danh mục thức ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật danh mục thức ăn: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (Integer id) {
        try {
            FoodCategoryEntity entity = repository.findOneById(id);
            if (entity == null) {
                return new ResponseEntity<>("Danh mục thức ăn không tồn tại!", HttpStatus.NOT_FOUND);
            }
            repository.deleteById(id);
            return ResponseEntity.ok().body("Xóa danh mục thức ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi xóa danh mục thức ăn: " + e.getMessage());
        }
    }
}
