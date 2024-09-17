package com.example.restaurant.mapper;

import com.example.restaurant.entity.SuppliersEntity;
import com.example.restaurant.request.SupplierRequest;
import com.example.restaurant.response.SupplierResponse;
import com.example.restaurant.utils.Slugify;
import com.example.restaurant.utils.TimeConvertUtil;

public class SupplierMapper {
    public static SuppliersEntity mapToEntity (SupplierRequest request) {
        SuppliersEntity entity = new SuppliersEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setSlug(Slugify.toSlug(request.getName()));
        entity.setAddress(request.getAddress());
        entity.setEmail(request.getEmail());
        entity.setPhoneNumber(request.getPhoneNumber());
        return entity;
    }

    public static SupplierResponse mapToResponse (SuppliersEntity entity) {
        SupplierResponse response = new SupplierResponse();
        response.setCode(entity.getCode());
        response.setName(entity.getName());
        response.setPhoneNumber(entity.getPhoneNumber());
        response.setEmail(entity.getEmail());
        response.setAddress(entity.getAddress());
        response.setCreateAt(TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt()));
        response.setUpdateAt(TimeConvertUtil.convertTimestampToDate(entity.getUpdatedAt()));
        return response;
    }
}
