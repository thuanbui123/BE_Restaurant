package com.example.restaurant.response;

import lombok.Data;

import java.util.List;

@Data
public class BillTableResponse {
    private Integer tableId;
    private String tableCode;
    private String tableLocation;
    private String tableStatus;
    private List<BillResponse> billResponses;
}
