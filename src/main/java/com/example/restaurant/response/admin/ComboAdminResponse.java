package com.example.restaurant.response.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComboAdminResponse {
    private Integer id;
    private String code;
    private String name;
    private String img;
    private Long price;
    private String description;
    private String status;
    private Integer soldCount;
    private String validFrom;
    private String validTo;
    private String createAt;
    private String updateAt;
}
