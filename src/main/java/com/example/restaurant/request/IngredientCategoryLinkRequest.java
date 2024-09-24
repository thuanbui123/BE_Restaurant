package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IngredientCategoryLinkRequest {
    @NotNull(message = "Mã nguyên liệu là một trường bắt buộc!")
    @Min(value = 1, message = "Mã nguyên liệu là một trường bắt buộc!")
    private Integer ingredientId;

    @NotNull(message = "Mã danh mục nguyên liệu là trường bắt buộc!")
    @Min(value = 1, message = "Mã danh mục nguyên liệu là trường bắt buộc!")
    private Integer categoryId;
}
