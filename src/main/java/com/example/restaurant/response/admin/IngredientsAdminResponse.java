package com.example.restaurant.response.admin;

import lombok.Data;

@Data
public class IngredientsAdminResponse {
    private String code;

    private String name;

    private String slug;

    private String img;

    private String type;

    private Float quantity;

    private String unit;

    private String supplierName;

    private String createAt;

    private String updateAt;
}
