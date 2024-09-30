package com.example.restaurant.response;

import lombok.Data;

@Data
public class OrderResponse {
    private String customerName;
    private String numberPhone;
    private String orderedDate;
    private String status;
}
