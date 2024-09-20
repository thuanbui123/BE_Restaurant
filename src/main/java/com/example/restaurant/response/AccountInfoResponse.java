package com.example.restaurant.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountInfoResponse {
    private Integer id;
    private String username;
}
