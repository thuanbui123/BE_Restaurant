package com.example.restaurant.mapper;

import com.example.restaurant.entity.IngredientCategoryEntity;
import com.example.restaurant.request.IngredientCategoryRequest;
import com.example.restaurant.response.admin.IngredientCategoryAdminResponse;
import com.example.restaurant.utils.Slugify;
import com.example.restaurant.utils.TimeConvertUtil;

public class IngredientCategoryMapper {
    public static IngredientCategoryEntity mapToEntity (IngredientCategoryRequest request) {
        IngredientCategoryEntity entity = new IngredientCategoryEntity();
        entity.setName(request.getName());
        entity.setSlug(Slugify.toSlug(request.getName()));
        entity.setDescription(request.getDescription());
        return entity;
    }

    public static IngredientCategoryAdminResponse mapToAdminResponse (IngredientCategoryEntity entity) {
        IngredientCategoryAdminResponse response = new IngredientCategoryAdminResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setSlug(entity.getSlug());
        response.setDescription(entity.getDescription());
        response.setCreateAt(TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt()));
        response.setUpdateAt(TimeConvertUtil.convertTimestampToDate(entity.getUpdatedAt()));
        return response;
    }
}
