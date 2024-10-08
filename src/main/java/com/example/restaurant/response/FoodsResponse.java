package com.example.restaurant.response;

import lombok.Data;

@Data
public class FoodsResponse {
    private Integer id;
    private String code;
    private String name;
    private String img;
    private Long price;
    private String description;
    private String createAt;
    private String updateAt;
}
