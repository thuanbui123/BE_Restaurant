package com.example.restaurant.mapper;

import com.example.restaurant.entity.CustomersEntity;
import com.example.restaurant.entity.OrderedEntity;
import com.example.restaurant.repository.CustomersRepository;
import com.example.restaurant.request.OrderRequest;
import com.example.restaurant.response.OrderResponse;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    private static CustomersRepository customersRepository;

    @Autowired
    public OrderMapper (CustomersRepository customersRepository) {
        OrderMapper.customersRepository = customersRepository;
    }

    public static OrderResponse mapToResponse (OrderedEntity entity) {
        OrderResponse response = new OrderResponse();
        response.setId(entity.getId());
        response.setCustomerName(entity.getCustomers().getName());
        response.setNumberPhone(entity.getCustomers().getPhoneNumber());
        response.setOrderedDate(TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt()));
        response.setStatus(entity.getStatus());
        return response;
    }

    public static OrderedEntity mapToEntity (OrderRequest request) {
        CustomersEntity customers = customersRepository.findOneById(request.getCustomerId());
        if (customers == null) {
            throw new IllegalArgumentException("Khách hàng không tồn tại: " + request.getCustomerId());
        }
        OrderedEntity entity = new OrderedEntity();
        entity.setStatus("Chờ xử lý");
        entity.setCustomers(customers);
        return entity;
    }
}
