package com.example.restaurant.mapper;

import com.example.restaurant.entity.FoodsEntity;
import com.example.restaurant.request.FoodsRequest;
import com.example.restaurant.response.FoodsResponse;
import com.example.restaurant.utils.Slugify;
import org.springframework.stereotype.Component;

@Component
public class FoodsMapper {
    public static FoodsEntity mapToEntity (FoodsRequest request) {
        FoodsEntity entity = new FoodsEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setSlug(Slugify.toSlug(request.getName()));
        entity.setImg(request.getImg());
        entity.setPrice(request.getPrice());
        entity.setDescription(request.getDescription());
        return entity;
    }

    public static FoodsResponse mapToResponse (FoodsEntity entity) {
        FoodsResponse response = new FoodsResponse();
        response.setCode(entity.getCode());
        response.setName(entity.getName());
        response.setPrice(entity.getPrice());
        response.setImg(entity.getImg());
        response.setDescription(entity.getDescription());
        return response;
    }
}
