package com.example.restaurant.mapper;

import com.example.restaurant.entity.EmployeeEntity;
import com.example.restaurant.entity.ImportInvoiceEntity;
import com.example.restaurant.request.ImportInvoiceRequest;
import com.example.restaurant.response.admin.ImportInvoiceResponse;
import com.example.restaurant.service.EmployeeService;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImportInvoiceMapper {
    private static EmployeeService service;

    @Autowired
    ImportInvoiceMapper(EmployeeService service) {
        ImportInvoiceMapper.service = service;
    }

    public static ImportInvoiceEntity mapToEntity (ImportInvoiceRequest request) {
        ImportInvoiceEntity entity = new ImportInvoiceEntity();
        entity.setCode(request.getCode());
        entity.setEntryDate(TimeConvertUtil.convertDateToLocalDateTime(request.getEntryDate()));
        EmployeeEntity employeeEntity = service.findOneById(request.getEmployeeId());
        if (employeeEntity == null) {
            return null;
        }
        entity.setEmployeeEntity(employeeEntity);
        return entity;
    }

    public static ImportInvoiceResponse mapToResponse (ImportInvoiceEntity entity) {
        ImportInvoiceResponse response = new ImportInvoiceResponse();
        response.setCode(entity.getCode());
        response.setEntryDate(TimeConvertUtil.convertLocalDateTimeToString(entity.getEntryDate()));
        response.setEmployeeName(entity.getEmployeeEntity().getName());
        return response;
    }
}
