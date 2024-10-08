package com.example.restaurant.service;

import com.example.restaurant.entity.EmbeddableId.FoodCategoryLinkId;
import com.example.restaurant.entity.FoodCategoryLinkEntity;
import com.example.restaurant.mapper.FoodCategoryLinkMapper;
import com.example.restaurant.repository.FoodCategoryLinkRepository;
import com.example.restaurant.request.FoodCategoryLinkRequest;
import com.example.restaurant.response.FoodCategoryLinkResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodCategoryLinkService {
    @Autowired
    private FoodCategoryLinkRepository repository;

    private ResponseEntity<?> findFoodsByCategoryId (Integer categoryId) {
        List<FoodCategoryLinkEntity> entities = repository.findFoodByCategoryId(categoryId);
        FoodCategoryLinkResponse linkResponses = FoodCategoryLinkMapper.mapToResponse(entities);
        if (linkResponses != null ) {
            linkResponses.sortIngredientsByCreateAt();
        }
        return ResponseEntity.ok().body(linkResponses);
    }

    public ResponseEntity<?> findData (String prefix, Integer query) {
        if (prefix.equals("find-foods-by-category-id") && query != null) {
            return findFoodsByCategoryId(query);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại");
    }

    public ResponseEntity<?> addData (FoodCategoryLinkRequest request) {
        try {
            FoodCategoryLinkId id = new FoodCategoryLinkId(request.getFoodId(), request.getCategoryId());
            if (repository.existsById(id)) {
                return ResponseEntity.badRequest().body("Món ăn đã tồn tại trong danh mục món ăn!");
            }

            FoodCategoryLinkEntity entity = FoodCategoryLinkMapper.mapToEntity(request);

            if (entity == null) {
                return ResponseEntity.badRequest().body("Món ăn hoặc danh mục món ăn không tồn tại!");
            }
            entity.setId(id);
            repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm món ăn vào danh mục món thành công!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (FoodCategoryLinkRequest request) {
        try {
            FoodCategoryLinkId id = new FoodCategoryLinkId(request.getFoodId(), request.getCategoryId());
            if (!repository.existsById(id)) {
                return ResponseEntity.badRequest().body("Món ăn không tồn tại trong danh mục món ăn!");
            }
            repository.deleteById(id);
            return ResponseEntity.ok().body("Xóa món ăn ra khỏi danh mục món ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
