package com.example.restaurant.mapper;

import com.example.restaurant.entity.BillEntity;
import com.example.restaurant.entity.BillTableEntity;
import com.example.restaurant.entity.TablesEntity;
import com.example.restaurant.request.BillTableRequest;
import com.example.restaurant.response.BillResponse;
import com.example.restaurant.response.BillTableResponse;
import com.example.restaurant.response.TablesResponse;
import com.example.restaurant.service.BillService;
import com.example.restaurant.service.TablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillTableMapper {
    private static BillService billService;
    private static TablesService tablesService;

    @Autowired
    BillTableMapper(BillService billService, TablesService tablesService) {
        BillTableMapper.billService = billService;
        BillTableMapper.tablesService = tablesService;
    }

    public static BillTableEntity mapToEntity (BillTableRequest request) {
        BillTableEntity entity = new BillTableEntity();
        BillEntity billEntity = billService.findOneById(request.getBillId());
        if (billEntity == null) return null;
        entity.setBill(billEntity);
        TablesEntity tablesEntity = tablesService.findById(request.getTableId());
        if (tablesEntity == null) return null;
        entity.setTable(tablesEntity);
        return entity;
    }

    public static BillTableResponse mapToResponse (List<BillTableEntity> entities) {
        BillTableResponse response = new BillTableResponse();
        response.setTableId(entities.get(0).getTable().getId());
        response.setTableCode(entities.get(0).getTable().getCode());
        response.setTableLocation(entities.get(0).getTable().getLocation());
        response.setTableStatus(entities.get(0).getTable().getStatus());
        List<BillResponse> billResponses = entities.stream()
                .map( entity -> {
                            BillEntity billEntity = entity.getBill();
                            return BillMapper.mapToResponse(billEntity);
                        }
                )
                .toList();
        response.setBillResponses(billResponses);
        return response;
    }
}
