package com.example.restaurant.response;

import lombok.Data;

@Data
public class SupplierResponse {
    private String code;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String createAt;
    private String updateAt;
}
