package com.example.restaurant.response;

import lombok.Data;

@Data
public class ComboOrderedDetailResponse {
    private Integer comboId;
    private String comboName;
    private String img;
    private Integer quantity;
    private Long totalPrice;
}
