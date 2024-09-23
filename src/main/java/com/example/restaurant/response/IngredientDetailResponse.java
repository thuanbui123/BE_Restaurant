package com.example.restaurant.response;

import lombok.Data;

@Data
public class IngredientDetailResponse {
    private String ingredientName;
    private Float quantity;
    private String unit;
    private String note;
}
