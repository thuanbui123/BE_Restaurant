package com.example.restaurant.mapper;

import com.example.restaurant.entity.AccountInfo;
import com.example.restaurant.entity.EmployeeEntity;
import com.example.restaurant.request.EmployeeRequest;
import com.example.restaurant.response.EmployeeResponse;
import com.example.restaurant.service.AccountService;
import com.example.restaurant.utils.Slugify;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    private static AccountService service;

    @Autowired
    public EmployeeMapper(AccountService service) {
        EmployeeMapper.service = service;
    }

    public static EmployeeEntity mapToEntity (EmployeeRequest request) {
        EmployeeEntity entity = new EmployeeEntity();
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

    public static EmployeeResponse mapToResponse (EmployeeEntity entity) {
        EmployeeResponse response = new EmployeeResponse();
        response.setCode(entity.getCode());
        response.setName(entity.getName());
        response.setPhoneNumber(entity.getPhoneNumber());
        response.setEmail(entity.getEmail());
        response.setAddress(entity.getAddress());
        response.setImg(entity.getAccount().getImg());
        response.setCreateAt(TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt()));
        response.setUpdateAt(TimeConvertUtil.convertTimestampToDate(entity.getUpdatedAt()));
        return response;
    }
}
