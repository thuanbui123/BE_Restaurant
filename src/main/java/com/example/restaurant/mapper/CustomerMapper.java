package com.example.restaurant.mapper;

import com.example.restaurant.entity.AccountInfo;
import com.example.restaurant.entity.CustomersEntity;
import com.example.restaurant.request.CustomerRequest;
import com.example.restaurant.response.CustomerResponse;
import com.example.restaurant.service.AccountService;
import com.example.restaurant.utils.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    private static AccountService service;

    @Autowired
    public CustomerMapper(AccountService service) {
        CustomerMapper.service = service;
    }

    public static CustomersEntity mapToEntity (CustomerRequest request) {
        CustomersEntity entity = new CustomersEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setSlug(Slugify.toSlug(request.getName()));
        entity.setAddress(request.getAddress());
        entity.setEmail(request.getEmail());
        entity.setPhoneNumber(request.getPhoneNumber());
        AccountInfo info = service.findOneById(request.getAccountId());
        if (info == null) {
            return null;
        }
        entity.setAccount(info);
        return entity;
    }

    public static CustomerResponse mapToResponse (CustomersEntity entity) {
        CustomerResponse response = new CustomerResponse();
        response.setCode(entity.getCode());
        response.setName(entity.getName());
        response.setPhoneNumber(entity.getPhoneNumber());
        response.setEmail(entity.getEmail());
        response.setAddress(entity.getAddress());
        response.setImg(entity.getAccount().getImg());
        return response;
    }
}
