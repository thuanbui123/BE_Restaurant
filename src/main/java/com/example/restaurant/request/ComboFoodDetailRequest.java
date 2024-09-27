package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComboFoodDetailRequest {


    @NotNull(message = "Mã món ăn là một trường bắt buộc!")
    @Min(value = 1, message = "Mã món ăn phải lớn hơn 0!")
    private Integer foodId;

    @NotNull(message = "Số lượng món ăn trong combo là trường bắt buộc!")
    @Min(value = 1, message = "Số lượng món ăn trong combo phải lớn hơn 0!")
    private Integer amountOfFood;
}
