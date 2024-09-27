package com.example.restaurant.response;

import lombok.Data;

import java.util.List;

@Data
public class FoodOrderedResponse {
    private List<FoodOrderedDetailResponse> responses;
}
