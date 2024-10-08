package com.example.restaurant.response;

import lombok.Data;

@Data
public class TablesResponse {
    private Integer id;
    private String code;
    private String location;
    private String status;
}
