package com.example.restaurant.response;

import lombok.Data;

@Data
public class AccountResponse {
    private String username;
    private String role;
    private String img;
    private String createdAt;
    private String updatedAt;
}
