package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderedTableRequest {
    @NotNull(message = "Mã khách hàng là một trường bắt buộc!")
    @Min(value = 1, message = "Mã khách hàng phải lớn hơn 0!")
    private Integer customerId;

    @NotNull(message = "Mã bàn ăn là trường bắt buộc!")
    @Min(value = 1, message = "Mã bàn ăn phải lớn hơn 0!")
    private Integer tableId;

    private Integer tableBookingId;
}
