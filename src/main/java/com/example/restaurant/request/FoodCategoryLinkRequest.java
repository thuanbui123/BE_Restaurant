package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FoodCategoryLinkRequest {
    @NotNull(message = "Mã món ăn là một trường bắt buộc!")
    @Min(value = 1, message = "Mã món ăn phải lớn hơn 0!")
    private Integer foodId;

    @NotNull(message = "Mã danh mục nguyên liệu là trường bắt buộc!")
    @Min(value = 1, message = "Mã danh mục nguyên liệu phải lớn hơn 0!")
    private Integer categoryId;
}
