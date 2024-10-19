package com.example.restaurant.service;

import com.example.restaurant.entity.OrderedEntity;
import com.example.restaurant.mapper.OrderMapper;
import com.example.restaurant.repository.OrderedRepository;
import com.example.restaurant.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    @Autowired
    private OrderedRepository repository;

    public ResponseEntity<?> getData(String prefix) {
        if (prefix.equals("get-order-now")) {
            List<OrderedEntity> orderedEntityList = repository.findStatus("Chờ xử lý");

            return ResponseEntity.ok(
                    orderedEntityList.stream()
                    .map(OrderMapper::mapToResponse)
                    .toList()
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
    }

    public ResponseEntity<?> addData (OrderRequest request) {
        try {
            OrderedEntity entity = OrderMapper.mapToEntity(request);
            repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
