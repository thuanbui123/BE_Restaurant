package com.example.restaurant.mapper;

import com.example.restaurant.entity.BlogPostEntity;
import com.example.restaurant.entity.EmployeeEntity;
import com.example.restaurant.request.BlogPostRequest;
import com.example.restaurant.response.BlogPostResponse;
import com.example.restaurant.service.EmployeeService;
import com.example.restaurant.utils.Slugify;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlogPostMapper {
    private static EmployeeService service;

    @Autowired
    BlogPostMapper(EmployeeService service) {
        BlogPostMapper.service = service;
    }

    public static BlogPostEntity mapToEntity (BlogPostRequest request) {
        BlogPostEntity entity = new BlogPostEntity();
        entity.setCode(request.getCode());
        entity.setTitle(request.getTitle());
        entity.setSlug(Slugify.toSlug(request.getTitle()));
        entity.setContent(request.getContent());
        EmployeeEntity employeeEntity = service.findOneById(request.getEmployeeId());
        if (employeeEntity == null) {
            return null;
        }
        entity.setEmployeeEntity(employeeEntity);
        return entity;
    }

    public static BlogPostResponse mapToResponse (BlogPostEntity entity) {
        BlogPostResponse response = new BlogPostResponse();
        response.setCode(entity.getCode());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setEmployeeName(entity.getEmployeeEntity().getName());
        response.setCreateAt(TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt()));
        response.setUpdateAt(TimeConvertUtil.convertTimestampToDate(entity.getUpdatedAt()));
        return response;
    }
}
