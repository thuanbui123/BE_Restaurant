package com.example.restaurant.response;

import lombok.Data;

@Data
public class FoodOrderedDetailResponse {
    private Integer foodId;
    private String foodName;
    private Integer quantity;
    private Long totalPrice;
}
