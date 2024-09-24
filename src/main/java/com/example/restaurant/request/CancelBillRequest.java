package com.example.restaurant.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelBillRequest {

    @NotBlank(message = "Ghi chú là một trường bắt buộc!")
    private String note;
}
