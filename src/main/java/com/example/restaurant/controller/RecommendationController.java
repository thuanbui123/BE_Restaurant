package com.example.restaurant.controller;

import com.example.restaurant.response.FoodsResponse;
import com.example.restaurant.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    @Autowired
    private RecommendationService service;

    /**
     * Lấy các món ăn gợi ý cho khách hàng hiện
     * từ các khách hàng tương đồng trong hệ thống
     * @param customerId
     * @param limit
     * @return
     */
    @GetMapping("/customer/{customerId}")
    public List<FoodsResponse> recommendFoods(@PathVariable Integer customerId, @RequestParam Integer limit) {
        return service.recommendFoodsForCustomer(customerId, limit);
    }
}
