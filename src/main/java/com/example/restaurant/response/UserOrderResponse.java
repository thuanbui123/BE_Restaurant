package com.example.restaurant.response;

import lombok.Data;

import java.util.List;

@Data
public class UserOrderResponse {
    private List<ComboOrderedDetailResponse> comboOrdered;
    private List<FoodOrderedDetailResponse> foodOrdered;
}
