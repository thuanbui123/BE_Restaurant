package com.example.restaurant.request;

import lombok.Data;

@Data
public class EmployeeOrderRequest {
    private FoodOrderedDetailRequest foodOrder;
    private ComboOrderedDetailRequest comboOrder;
}
