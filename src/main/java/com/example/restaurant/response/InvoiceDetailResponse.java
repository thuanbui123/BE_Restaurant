package com.example.restaurant.response;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceDetailResponse {
    private String code;
    private Integer importInvoiceId;
    private List<IngredientDetailResponse> ingredient;
}
