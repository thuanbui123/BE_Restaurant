package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IngredientDetailRequest {
    @NotNull(message = "Mã nguyên liệu là trường bắt buộc!")
    @Min(value = 1, message = "Mã nguyên liệu phải lớn hơn 0!")
    private Integer ingredientId;

    @NotNull(message = "Số lượng nguyên liệu là trường bắt buộc!")
    @Min(value = 1, message = "Số lượng nguyên liệu phải lớn hơn 0!")
    private Float quantity;

    @NotBlank(message = "Đơn vị là một trường bắt buộc!")
    private String unit;

    private String note;
}
