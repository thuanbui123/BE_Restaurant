package com.example.restaurant.response;

import lombok.Data;

@Data
public class BillResponse {
    private String code;
    private String employeeName;
    private String status;
    private Long totalPrice;
    private String note;
    private String orderTime;
}
