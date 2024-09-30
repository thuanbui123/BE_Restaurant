package com.example.restaurant.mapper;

import com.example.restaurant.entity.OrderedEntity;
import com.example.restaurant.response.OrderResponse;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public static OrderResponse mapToResponse (OrderedEntity entity) {
        OrderResponse response = new OrderResponse();
        response.setCustomerName(entity.getCustomer().getName());
        response.setNumberPhone(entity.getCustomer().getPhoneNumber());
        response.setOrderedDate(TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt()));
        response.setStatus(entity.getStatus());
        return response;
    }
}
