package com.example.restaurant.response;

import lombok.Data;

@Data
public class ComboResponse {
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
}
