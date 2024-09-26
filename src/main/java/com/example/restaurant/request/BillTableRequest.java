package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BillTableRequest {
    @NotNull(message = "Mã hóa đơn là một trường bắt buộc!")
    @Min(value = 1, message = "Mã hóa đơn là một trường bắt buộc!")
    private Integer billId;

    @NotNull(message = "Mã bàn ăn là trường bắt buộc!")
    @Min(value = 1, message = "Mã bàn ăn là trường bắt buộc!")
    private Integer tableId;
}
