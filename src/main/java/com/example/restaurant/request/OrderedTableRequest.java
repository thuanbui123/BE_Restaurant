package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderedTableRequest {
    @NotNull(message = "Mã đơn hàng là một trường bắt buộc!")
    @Min(value = 1, message = "Mã đơn hàng là một trường bắt buộc!")
    private Integer orderedId;

    @NotNull(message = "Mã bàn ăn là trường bắt buộc!")
    @Min(value = 1, message = "Mã bàn ăn là trường bắt buộc!")
    private Integer tableId;
}
