package com.example.restaurant.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IngredientCategoryRequest {
    @NotBlank(message = "Tên danh mục nguyên liệu là một trường dữ liệu bắt buộc!")
    private String name;
    @NotBlank(message = "Mô tả danh mục nguyên liệu là một trường dữ liệu bắt buộc!")
    private String description;
}
