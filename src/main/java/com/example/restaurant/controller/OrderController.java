package com.example.restaurant.controller;

import com.example.restaurant.request.OrderRequest;
import com.example.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService service;

    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix) {
        return service.getData(prefix);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addData (@RequestBody OrderRequest request) {
        return service.addData(request);
    }
}
