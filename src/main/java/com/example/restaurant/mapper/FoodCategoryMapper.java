package com.example.restaurant.mapper;

import com.example.restaurant.entity.FoodCategoryEntity;
import com.example.restaurant.entity.FoodsEntity;
import com.example.restaurant.request.FoodCategoryRequest;
import com.example.restaurant.response.FoodCategoryAdminResponse;
import com.example.restaurant.response.FoodCategoryUserResponse;
import com.example.restaurant.service.FoodsService;
import com.example.restaurant.utils.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FoodCategoryMapper {
    private static FoodsService foodsService;
    public FoodCategoryMapper(){}

    @Autowired
    public FoodCategoryMapper(FoodsService foodsService) {
        FoodCategoryMapper.foodsService = foodsService;
    }

    public static FoodCategoryEntity mapToEntity (FoodCategoryRequest request) {
        FoodCategoryEntity entity = new FoodCategoryEntity();
        entity.setName(request.getName());
        entity.setSlug(Slugify.toSlug(request.getName()));
        entity.setDescription(request.getDescription());
        List<Integer> idList = request.getFoodIdList();
        if (idList != null && !idList.isEmpty()) {
            List<FoodsEntity> foodsEntityList = new ArrayList<>();
            for (Integer i : idList) {
                foodsEntityList.add(foodsService.findOneById(i));
            }
            entity.setFoodsEntityList(foodsEntityList);
        }
        return entity;
    }

    public static FoodCategoryUserResponse mapToUserResponse (FoodCategoryEntity entity) {
        FoodCategoryUserResponse userResponse = new FoodCategoryUserResponse();
        userResponse.setId(entity.getId());
        userResponse.setName(entity.getName());
        return userResponse;
    }

    public static FoodCategoryAdminResponse mapToAdminResponse (FoodCategoryEntity entity) {
        FoodCategoryAdminResponse adminResponse = new FoodCategoryAdminResponse();
        adminResponse.setId(entity.getId());
        adminResponse.setName(entity.getName());
        adminResponse.setDescription(entity.getDescription());
        return adminResponse;
    }
}
