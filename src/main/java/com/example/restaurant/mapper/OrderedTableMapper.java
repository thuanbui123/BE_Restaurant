package com.example.restaurant.mapper;

import com.example.restaurant.entity.OrderedEntity;
import com.example.restaurant.entity.OrderedTableEntity;
import com.example.restaurant.entity.TablesEntity;
import com.example.restaurant.repository.OrderedRepository;
import com.example.restaurant.request.OrderedTableRequest;
import com.example.restaurant.response.OrderedTableResponse;
import com.example.restaurant.response.OrderResponse;
import com.example.restaurant.service.TablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderedTableMapper {
    private static OrderedRepository orderedRepository;
    private static TablesService tablesService;

    @Autowired
    OrderedTableMapper(OrderedRepository orderedRepository, TablesService tablesService) {
        OrderedTableMapper.orderedRepository = orderedRepository;
        OrderedTableMapper.tablesService = tablesService;
    }

    public static OrderedTableEntity mapToEntity (OrderedTableRequest request) {
        OrderedTableEntity entity = new OrderedTableEntity();
        OrderedEntity ordered = orderedRepository.findOneById(request.getOrderedId());
        if (ordered == null) return null;
        TablesEntity tablesEntity = tablesService.findById(request.getTableId());
        if (tablesEntity == null) return null;
        entity.setOrdered(ordered);
        entity.setTable(tablesEntity);
        return entity;
    }

    public static OrderedTableResponse mapToResponse (List<OrderedTableEntity> entities) {
        OrderedTableResponse response = new OrderedTableResponse();
        response.setTableId(entities.get(0).getTable().getId());
        response.setTableCode(entities.get(0).getTable().getCode());
        response.setTableLocation(entities.get(0).getTable().getLocation());
        response.setTableStatus(entities.get(0).getTable().getStatus());
        List<OrderResponse> ordered = entities.stream()
                .map( entity -> {
                            OrderedEntity orderedEntity = entity.getOrdered();
                            return OrderMapper.mapToResponse(orderedEntity);
                        }
                )
                .toList();
        response.setOrderResponses(ordered);
        return response;
    }
}
