package com.example.restaurant.response;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeOrderResponse {
    private String code;
    private String dateOrder;
    private String datePayment;
    private String customerCode;
    private String customerName;
    private String numberPhone;
    private String address;
    private String tableCode;
    private String location;
    private Long totalPrice;
    private List<ComboOrderedDetailResponse> comboOrdered;
    private List<FoodOrderedDetailResponse> foodOrdered;
}
