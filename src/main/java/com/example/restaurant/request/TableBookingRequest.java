package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TableBookingRequest {
    @NotNull(message = "Mã khách hàng là một trường bắt buộc!")
    @Min(value = 1, message = "Mã khách hàng phải lớn hơn 0!")
    private Integer customerId;

    @NotBlank(message = "Thời gian dự kiến dùng bữa là một trường bắt buộc!")
    private String intervalTime;

    private String note;
}
