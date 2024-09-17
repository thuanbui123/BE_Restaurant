package com.example.restaurant.mapper;

import com.example.restaurant.entity.IngredientsEntity;
import com.example.restaurant.entity.SuppliersEntity;
import com.example.restaurant.request.IngredientsRequest;
import com.example.restaurant.response.admin.IngredientsAdminResponse;
import com.example.restaurant.service.SupplierService;
import com.example.restaurant.utils.Slugify;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IngredientsMapper {
    private static SupplierService service;

    @Autowired
    IngredientsMapper(SupplierService service) {
        IngredientsMapper.service = service;
    }

    public static IngredientsEntity mapToEntity (IngredientsRequest request) {
        IngredientsEntity entity = new IngredientsEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setSlug(Slugify.toSlug(request.getName()));
        entity.setImg(request.getImg());
        entity.setQuantity(request.getQuantity());
        entity.setType(request.getType());
        entity.setUnit(request.getUnit());

        SuppliersEntity suppliersEntity = service.findOneById(request.getSupplierId());
        if (suppliersEntity == null) {
            return null;
        }

        entity.setSuppliersEntity(suppliersEntity);
        return entity;
    }

    public static IngredientsAdminResponse mapToAdminResponse (IngredientsEntity entity) {
        IngredientsAdminResponse response = new IngredientsAdminResponse();
        response.setCode(entity.getCode());
        response.setName(entity.getName());
        response.setSlug(entity.getSlug());
        response.setQuantity(entity.getQuantity());
        response.setType(entity.getType());
        response.setUnit(entity.getUnit());
        response.setImg(entity.getImg());
        response.setSupplierName(entity.getSuppliersEntity().getName());
        response.setCreateAt(TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt()));
        response.setUpdateAt(TimeConvertUtil.convertTimestampToDate(entity.getUpdatedAt()));
        return response;
    }
}
