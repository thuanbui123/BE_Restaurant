package com.example.restaurant.request;

import lombok.Data;

import java.util.List;

@Data
public class ComboOrderedRequest {
    private Integer ordered;
    private List<ComboOrderedDetailRequest> requests;
}
