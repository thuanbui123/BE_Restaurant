package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IngredientsRequest {
    @NotBlank(message = "Mã nguyên liệu là trường dữ liệu bắt buộc!")
    private String code;

    @NotBlank(message = "Tên nguyên liệu là trường dữ liệu bắt buộc!")
    private String name;

    @NotBlank(message = "Ảnh của nguyên liệu là trường dữ liệu bắt buộc!")
    private String img;

    @NotBlank(message = "Loại nguyên liệu là trường dữ liệu bắt buộc!")
    private String type;

    @NotNull(message = "Số lượng nguyên liệu là trường dữ liệu bắt buộc!")
    @Min(value = 0, message = "Số lượng nguyên liệu phải lớn hơn hoặc bằng 0!")
    private Float quantity;

    @NotBlank(message = "Đơn vị của nguyên liệu là trường dữ liệu bắt buộc!")
    private String unit;

    @NotNull(message = "Mã nhà cung cấp là trường dữ liệu bắt buộc!")
    @Min(value = 1, message = "Mã nhà cung câp phải lớn hơn 0!")
    private Integer supplierId;
}
