package com.example.restaurant.response;

import lombok.Data;

@Data
public class InvoiceResponse {
    private String code;
    private Integer importInvoiceId;
    private String entryDate;
}
