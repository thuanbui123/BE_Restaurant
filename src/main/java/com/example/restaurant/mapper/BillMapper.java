package com.example.restaurant.mapper;

import com.example.restaurant.entity.BillEntity;
import com.example.restaurant.entity.EmployeeEntity;
import com.example.restaurant.request.BillRequest;
import com.example.restaurant.response.BillResponse;
import com.example.restaurant.service.EmployeeService;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillMapper {
    private static EmployeeService employeeService;

    @Autowired
    BillMapper(EmployeeService employeeService) {
        BillMapper.employeeService = employeeService;
    }

    public static BillEntity mapToEntity (BillRequest request) {
        BillEntity entity = new BillEntity();
        entity.setCode(request.getCode());

        EmployeeEntity employee = employeeService.findOneById(request.getEmployeeId());

        if (employee == null) {
            return null;
        }

        entity.setEmployee(employee);
        entity.setTotalPrice(0L);
        entity.setStatus(request.getStatus());
        entity.setNote(request.getNote());

        return entity;
    }

    public static BillResponse mapToResponse (BillEntity entity) {
        BillResponse response = new BillResponse();

        response.setCode(entity.getCode());
        response.setOrderTime(TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt()));
        response.setStatus(entity.getStatus());
        response.setNote(entity.getNote());
        response.setTotalPrice(entity.getTotalPrice());
        response.setEmployeeName(entity.getEmployee().getName());

        return response;
    }
}
