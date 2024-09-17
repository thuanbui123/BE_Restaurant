package com.example.restaurant.mapper;

import com.example.restaurant.entity.ComboEntity;
import com.example.restaurant.request.ComboRequest;
import com.example.restaurant.response.ComboResponse;
import com.example.restaurant.utils.Slugify;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.stereotype.Component;

@Component
public class ComboMapper {
    public static ComboEntity mapToEntity (ComboRequest request) {
        ComboEntity entity = new ComboEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setSlug(Slugify.toSlug(request.getName()));
        entity.setImg(request.getImg());
        entity.setPrice(request.getPrice());
        entity.setDescription(request.getDescription());
        return entity;
    }

    public static ComboResponse mapToResponse (ComboEntity entity) {
        ComboResponse response = new ComboResponse();
        response.setCode(entity.getCode());
        response.setName(entity.getName());
        response.setImg(entity.getImg());
        response.setPrice(entity.getPrice());
        response.setDescription(entity.getDescription());
        response.setCreateAt(TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt()));
        response.setUpdateAt(TimeConvertUtil.convertTimestampToDate(entity.getUpdatedAt()));
        return response;
    }
}
