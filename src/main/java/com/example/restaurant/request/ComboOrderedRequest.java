package com.example.restaurant.request;

import lombok.Data;

import java.util.List;

@Data
public class ComboOrderedRequest {
    private Integer billId;
    private List<ComboOrderedDetailRequest> requests;
}
