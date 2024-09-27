package com.example.restaurant.response;

import lombok.Data;

import java.util.List;

@Data
public class ComboOrderedResponse {
    private Integer billId;
    private String billCode;
    private String customerName;
    private String employeeName;
    private String billStatus;
    private Long billTotalPrice;
    private String billNote;
    private List<ComboOrderedDetailResponse> comboResponses;
}
