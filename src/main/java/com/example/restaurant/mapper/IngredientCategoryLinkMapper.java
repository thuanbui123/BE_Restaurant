package com.example.restaurant.mapper;

import com.example.restaurant.entity.IngredientCategoryEntity;
import com.example.restaurant.entity.IngredientCategoryLinkEntity;
import com.example.restaurant.entity.IngredientsEntity;
import com.example.restaurant.request.IngredientCategoryLinkRequest;
import com.example.restaurant.response.IngredientCategoryLinkResponse;
import com.example.restaurant.response.admin.IngredientsAdminResponse;
import com.example.restaurant.service.IngredientCategoryService;
import com.example.restaurant.service.IngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IngredientCategoryLinkMapper {
    private static IngredientsService ingredientsService;
    private static IngredientCategoryService ingredientCategoryService;

    @Autowired
    IngredientCategoryLinkMapper (IngredientsService ingredientsService, IngredientCategoryService ingredientCategoryService) {
        IngredientCategoryLinkMapper.ingredientCategoryService = ingredientCategoryService;
        IngredientCategoryLinkMapper.ingredientsService = ingredientsService;
    }

    public static IngredientCategoryLinkEntity mapToEntity (IngredientCategoryLinkRequest request) {
        IngredientCategoryLinkEntity entity = new IngredientCategoryLinkEntity();
        IngredientsEntity ingredientsEntity = ingredientsService.findById(request.getIngredientId());
        if (ingredientsEntity == null) return null;
        entity.setIngredientsEntity(ingredientsEntity);
        IngredientCategoryEntity ingredientCategoryEntity = ingredientCategoryService.findById(request.getCategoryId());
        if (ingredientCategoryEntity == null) return null;
        entity.setIngredientCategoryEntity(ingredientCategoryEntity);
        return entity;
    }

    public static IngredientCategoryLinkResponse mapToResponse (List<IngredientCategoryLinkEntity> entities) {
        IngredientCategoryLinkResponse response = new IngredientCategoryLinkResponse();
        response.setIngredientCategoryName(entities.get(0).getIngredientCategoryEntity().getName());
        List<IngredientsAdminResponse> ingredientsAdminResponses = entities.stream()
                .map( entity -> {
                            IngredientsEntity ingredient = entity.getIngredientsEntity();
                            return IngredientsMapper.mapToAdminResponse(ingredient);
                        }
                )
                .toList();
        response.setIngredientsAdminResponses(ingredientsAdminResponses);
        return response;
    }
}
