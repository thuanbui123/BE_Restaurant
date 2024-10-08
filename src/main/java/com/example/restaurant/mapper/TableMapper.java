package com.example.restaurant.mapper;

import com.example.restaurant.entity.TablesEntity;
import com.example.restaurant.request.TablesRequest;
import com.example.restaurant.response.TablesResponse;

public class TableMapper {
    public static TablesEntity mapToEntity (TablesRequest request) {
        TablesEntity entity = new TablesEntity();
        entity.setCode(request.getCode());
        entity.setLocation(request.getLocation());
        entity.setStatus("Trống");
        return entity;
    }

    public static TablesResponse mapToResponse (TablesEntity entity) {
        TablesResponse response = new TablesResponse();
        response.setId(entity.getId());
        response.setCode(entity.getCode());
        response.setLocation(entity.getLocation());
        response.setStatus(entity.getStatus());
        return response;
    }
}
