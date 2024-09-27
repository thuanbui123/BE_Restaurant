package com.example.restaurant.response;

import lombok.Data;

import java.util.List;

@Data
public class ComboFoodResponse {
    private Integer comboId;
    private String comboName;
    private String img;
    private Long totalPrice;
    private String description;
    private String status;
    private List<ComboFoodDetailResponse> foodDetailResponses;
}
