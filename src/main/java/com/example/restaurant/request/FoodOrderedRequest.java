package com.example.restaurant.request;

import lombok.Data;

import java.util.List;

@Data
public class FoodOrderedRequest {
    private Integer orderedId;
    private List<FoodOrderedDetailRequest> detailRequests;
}
