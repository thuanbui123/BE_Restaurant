package com.example.restaurant.response.admin;

import lombok.Data;

@Data
public class ComboAdminResponse {
    private String code;
    private String name;
    private String img;
    private Long price;
    private String description;
    private String createdAt;
    private String updatedAt;
}
