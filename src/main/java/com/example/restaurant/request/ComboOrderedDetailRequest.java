package com.example.restaurant.request;

import lombok.Data;

@Data
public class ComboOrderedDetailRequest {
    private Integer comboId;
    private Integer quantity;
}
