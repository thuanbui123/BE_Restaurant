package com.example.restaurant.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class FoodCategoryRequest {

    @NotBlank(message="Tên danh mục món ăn là một trường bắt buộc!")
    private String name;

    @NotBlank(message = "Mô tả danh mục món ăn là một trường bắt buôc.")
    private String description;

    private List<Integer> foodIdList;
}
