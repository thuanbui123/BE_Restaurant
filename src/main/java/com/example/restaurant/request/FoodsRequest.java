package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FoodsRequest {
    @NotBlank(message = "Mã món ăn là một trường dữ liệu bắt buộc!")
    @Pattern(regexp = "^[^\\s]{6,10}$", message = "Mã món ăn phải dài từ 6 đến 10 ký tự và không chứa khoảng trắng!")
    private String code;

    @NotBlank(message = "Tên món ăn là một trường dữ liệu bắt buộc!")
    private String name;

    @NotBlank(message = "Ảnh của món ăn là một trường dữ liệu bắt buộc!")
    private String img;

    @NotNull(message = "Giá của món ăn không được để trống")
    @Min(value = 1, message = "Giá của món ăn phải lớn hơn 0")
    private Long price;

    @NotBlank(message = "Mô tả món ăn là một trường dữ liệu bắt buộc!")
    private String description;
}
