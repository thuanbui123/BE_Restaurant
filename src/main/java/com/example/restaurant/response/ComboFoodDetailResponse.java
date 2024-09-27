package com.example.restaurant.response;

import lombok.Data;

@Data
public class ComboFoodDetailResponse {
    private Integer id;
    private String code;
    private String name;
    private String img;
    private Long price;
    private Integer amountOfFood;
    private String description;
}
