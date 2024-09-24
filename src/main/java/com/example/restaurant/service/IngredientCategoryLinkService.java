package com.example.restaurant.service;

import com.example.restaurant.entity.EmbeddableId.IngredientCategoryLinkId;
import com.example.restaurant.entity.IngredientCategoryLinkEntity;
import com.example.restaurant.mapper.IngredientCategoryLinkMapper;
import com.example.restaurant.repository.IngredientCategoryLinkRepository;
import com.example.restaurant.request.IngredientCategoryLinkRequest;
import com.example.restaurant.response.IngredientCategoryLinkResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientCategoryLinkService {
    @Autowired
    private IngredientCategoryLinkRepository repository;

    private ResponseEntity<?> findIngredientByCategoryId (Integer categoryId) {
        List<IngredientCategoryLinkEntity> entities = repository.findIngredientsByCategoryId(categoryId);
        IngredientCategoryLinkResponse linkResponses = IngredientCategoryLinkMapper.mapToResponse(entities);
        linkResponses.sortIngredientsByCreateAt();
        return ResponseEntity.ok().body(linkResponses);
    }

    public ResponseEntity<?> findData (String prefix, Integer query) {
        if (prefix.equals("find-ingredient-by-category-id") && query != null) {
            return findIngredientByCategoryId(query);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại");
    }

    public ResponseEntity<?> addData (IngredientCategoryLinkRequest request) {
        try {
            IngredientCategoryLinkId id = new IngredientCategoryLinkId(request.getIngredientId(), request.getCategoryId());
            if (repository.existsById(id)) {
                return ResponseEntity.badRequest().body("Nguyên liệu đã tồn tại trong danh mục nguyên liệu!");
            }

            IngredientCategoryLinkEntity entity = IngredientCategoryLinkMapper.mapToEntity(request);

            if (entity == null) {
                return ResponseEntity.badRequest().body("Nguyên liệu hoặc danh mục nguyên liệu không tồn tại!");
            }
            entity.setId(id);
            repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm nguyên liệu vào danh mục nguyên liệu thành công!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (IngredientCategoryLinkRequest request) {
        try {
            IngredientCategoryLinkId id = new IngredientCategoryLinkId(request.getIngredientId(), request.getCategoryId());
            if (!repository.existsById(id)) {
                return ResponseEntity.badRequest().body("Nguyên liệu không tồn tại trong danh mục nguyên liệu!");
            }
            repository.deleteById(id);
            return ResponseEntity.ok().body("Xóa nguyên liệu ra khỏi danh mục nguyên liệu thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
