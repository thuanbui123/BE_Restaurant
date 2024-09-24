package com.example.restaurant.mapper;

import com.example.restaurant.entity.BillEntity;
import com.example.restaurant.entity.CustomersEntity;
import com.example.restaurant.entity.EmployeeEntity;
import com.example.restaurant.request.BillRequest;
import com.example.restaurant.response.BillResponse;
import com.example.restaurant.service.CustomerService;
import com.example.restaurant.service.EmployeeService;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillMapper {
    private static EmployeeService employeeService;
    private static CustomerService customerService;

    @Autowired
    BillMapper(EmployeeService employeeService, CustomerService customerService) {
        BillMapper.employeeService = employeeService;
        BillMapper.customerService = customerService;
    }

    public static BillEntity mapToEntity (BillRequest request) {
        BillEntity entity = new BillEntity();
        entity.setCode(request.getCode());

        EmployeeEntity employee = employeeService.findOneById(request.getEmployeeId());
        CustomersEntity customersEntity = customerService.findOneById(request.getCustomerId());

        if (employee == null || customersEntity == null) {
            return null;
        }

        entity.setEmployee(employee);
        entity.setCustomer(customersEntity);
        entity.setTotalPrice(request.getTotalPrice());
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
        response.setCustomerName(entity.getCustomer().getName());

        return response;
    }
}
