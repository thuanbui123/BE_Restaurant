package com.example.restaurant.mapper;

import com.example.restaurant.entity.CustomersEntity;
import com.example.restaurant.entity.TableBookingEntity;
import com.example.restaurant.repository.CustomersRepository;
import com.example.restaurant.repository.TablesRepository;
import com.example.restaurant.request.TableBookingRequest;
import com.example.restaurant.response.TableBookingResponse;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TableBookingMapper {
    private static CustomersRepository customersRepository;
    private static TablesRepository tablesRepository;

    @Autowired
    TableBookingMapper (CustomersRepository customersRepository, TablesRepository tablesRepository) {
        TableBookingMapper.customersRepository = customersRepository;
        TableBookingMapper.tablesRepository = tablesRepository;
    }

    public static TableBookingEntity mapToEntity (TableBookingRequest request) {
        if (!customersRepository.existsById(request.getCustomerId())) {
            throw new IllegalArgumentException("Không tồn tại khách hàng " + request.getCustomerId());
        }
        TableBookingEntity entity = new TableBookingEntity();
        entity.setBookingTime(request.getBookingTime());
        entity.setTablesEntity(tablesRepository.findOneById(1));
        entity.setNote(request.getNote());
        entity.setStatus("Đã đặt bàn");
        entity.setCustomer(customersRepository.findOneById(request.getCustomerId()));
        return entity;
    }

    public static TableBookingResponse mapToResponse (TableBookingEntity entity) {
        TableBookingResponse response = new TableBookingResponse();
        response.setId(entity.getId());
        response.setStatus(entity.getStatus());
        response.setTableId(entity.getTablesEntity().getId());
        response.setNote(entity.getNote());
        response.setBookingTime(entity.getBookingTime());
        CustomersEntity customersEntity = entity.getCustomer();
        response.setCustomerId(customersEntity.getId());
        response.setCustomerName(customersEntity.getName());
        return response;
    }
}
