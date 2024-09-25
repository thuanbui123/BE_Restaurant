package com.example.restaurant.mapper;

import com.example.restaurant.entity.FoodCategoryEntity;
import com.example.restaurant.entity.FoodCategoryLinkEntity;
import com.example.restaurant.entity.FoodsEntity;
import com.example.restaurant.request.FoodCategoryLinkRequest;
import com.example.restaurant.response.FoodCategoryLinkResponse;
import com.example.restaurant.response.FoodsResponse;
import com.example.restaurant.service.FoodCategoryService;
import com.example.restaurant.service.FoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FoodCategoryLinkMapper {
    private static FoodsService foodsService;
    private static FoodCategoryService foodCategoryService;

    @Autowired
    FoodCategoryLinkMapper (FoodsService foodsService, FoodCategoryService foodCategoryService) {
        FoodCategoryLinkMapper.foodsService = foodsService;
        FoodCategoryLinkMapper.foodCategoryService = foodCategoryService;
    }

    public static FoodCategoryLinkEntity mapToEntity (FoodCategoryLinkRequest request) {
        FoodCategoryLinkEntity entity = new FoodCategoryLinkEntity();
        FoodsEntity foodsEntity = foodsService.findOneById(request.getFoodId());
        if (foodsEntity == null) return null;
        entity.setFoods(foodsEntity);
        FoodCategoryEntity foodCategoryEntity = foodCategoryService.findOneById(request.getCategoryId());
        if (foodCategoryEntity == null) return null;
        entity.setFoodCategory(foodCategoryEntity);
        return entity;
    }

    public static FoodCategoryLinkResponse mapToResponse (List<FoodCategoryLinkEntity> entities) {
        FoodCategoryLinkResponse response = new FoodCategoryLinkResponse();
        response.setFoodCategoryName(entities.get(0).getFoodCategory().getName());
        List<FoodsResponse> foodsResponses = entities.stream()
                .map( entity -> {
                            FoodsEntity foods = entity.getFoods();
                            return FoodsMapper.mapToResponse(foods);
                        }
                )
                .toList();
        response.setFoodsResponses(foodsResponses);
        return response;
    }
}
