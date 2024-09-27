package com.example.restaurant.request;

import lombok.Data;

@Data
public class FoodOrderedDetailRequest {
    private Integer foodId;
    private Integer quantity;
}
