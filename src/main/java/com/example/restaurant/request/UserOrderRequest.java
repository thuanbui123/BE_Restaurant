package com.example.restaurant.request;

import lombok.Data;

@Data
public class UserOrderRequest {
    private FoodOrderedDetailRequest foodOrder;
    private ComboOrderedDetailRequest comboOrder;
}
