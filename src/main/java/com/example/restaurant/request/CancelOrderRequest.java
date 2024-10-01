package com.example.restaurant.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelOrderRequest {
    @NotBlank(message = "Phải nhập lý do khi hủy đơn hàng!")
    private String note;
}
