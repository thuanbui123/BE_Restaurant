package com.example.restaurant.response;

import lombok.Data;

@Data
public class TableBookingResponse {
    private Integer id;
    private Integer customerId;
    private String customerName;
    private String bookingTime;
    private Integer tableId;
    private String status;
    private String note;
}
