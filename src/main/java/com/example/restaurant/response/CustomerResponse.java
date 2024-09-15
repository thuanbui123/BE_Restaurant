package com.example.restaurant.response;

import lombok.Data;

@Data
public class CustomerResponse {
    private String code;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String img;
}
