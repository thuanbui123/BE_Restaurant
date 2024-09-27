package com.example.restaurant.mapper;

import com.example.restaurant.entity.ComboEntity;
import com.example.restaurant.request.ComboRequest;
import com.example.restaurant.response.ComboResponse;
import com.example.restaurant.response.admin.ComboAdminResponse;
import com.example.restaurant.utils.Slugify;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;

@Component
public class ComboMapper {
    public static ComboEntity mapToEntity (ComboRequest request) throws ParseException {
        ComboEntity entity = new ComboEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setSlug(Slugify.toSlug(request.getName()));
        entity.setImg(request.getImg());
        entity.setDescription(request.getDescription());
        entity.setPrice(0L);
        entity.setSoldCount(request.getSoldCount());
        entity.setValidFrom(TimeConvertUtil.convertDateToTimestamp(request.getValidFrom()));
        entity.setValidTo(TimeConvertUtil.convertDateToTimestamp(request.getValidTo()));
        return entity;
    }

    public static ComboAdminResponse mapToResponse (ComboEntity entity) {
        ComboAdminResponse response = new ComboAdminResponse();
        response.setId(entity.getId());
        response.setCode(entity.getCode());
        response.setName(entity.getName());
        response.setImg(entity.getImg());
        response.setPrice(entity.getPrice());
        response.setStatus(entity.getStatus());
        response.setDescription(entity.getDescription());
        response.setStatus(entity.getStatus());
        response.setSoldCount(entity.getSoldCount());
        response.setValidFrom(TimeConvertUtil.convertTimestampToDate(entity.getValidFrom()));
        response.setValidTo(TimeConvertUtil.convertTimestampToDate(entity.getValidTo()));
        response.setCreateAt(TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt()));
        response.setUpdateAt(TimeConvertUtil.convertTimestampToDate(entity.getUpdatedAt()));
        return response;
    }

    public static ComboResponse mapToUserResponse (ComboEntity entity) {
        ComboResponse response = new ComboResponse();
        response.setId(entity.getId());
        response.setCode(entity.getCode());
        response.setName(entity.getName());
        response.setImg(entity.getImg());
        response.setPrice(entity.getPrice());
        response.setStatus(entity.getStatus());
        response.setDescription(entity.getDescription());
        response.setStatus(entity.getStatus());
        response.setSoldCount(entity.getSoldCount());
        response.setValidFrom(TimeConvertUtil.convertTimestampToDate(entity.getValidFrom()));
        response.setValidTo(TimeConvertUtil.convertTimestampToDate(entity.getValidTo()));
        return response;
    }
}
