package com.example.restaurant.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TablesRequest {

    @NotBlank(message = "Mã bàn ăn là một trường dữ liệu bắt buộc!")
    private String code;

    @NotBlank(message = "Vị trí của bàn ăn là một trường dữ liệu bắt buộc!")
    private String location;

    private String status;
}
