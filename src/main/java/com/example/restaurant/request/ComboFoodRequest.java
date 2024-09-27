package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ComboFoodRequest {

    @NotNull(message = "Mã combo là trường bắt buộc!")
    @Min(value = 1, message = "Mã combo phải lớn hơn 0!")
    private Integer comboId;

    @NotNull(message = "Danh sách món ăn trong combo là trường bắt buộc")
    private List<ComboFoodDetailRequest> detailRequests;
}
