package com.example.restaurant.response.admin;

import lombok.Data;

@Data
public class IngredientCategoryAdminResponse {
    private Integer id;
    private String name;
    private String slug;
    private String description;
}
