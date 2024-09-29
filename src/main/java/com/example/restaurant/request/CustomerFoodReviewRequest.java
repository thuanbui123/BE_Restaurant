package com.example.restaurant.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerFoodReviewRequest {
    @NotNull(message = "Mã khách hàng là một trường bắt buộc!")
    @Min(value = 1, message = "Mã khách hàng phải lớn hơn 0!")
    private Integer customerId;

    @NotNull(message = "Mã món ăn khách hàng là một trường bắt buộc!")
    @Min(value = 1, message = "Mã món ăn khách hàng phải lớn hơn 0!")
    private Integer foodId;

    @NotNull(message = "Số sao đánh giá là một trường bắt buộc!")
    @Min(value = 1, message = "Số sao đánh giá phải lớn hơn 0!")
    @Max(value = 5, message = "Số sao phải nhỏ hơn hoặc bằng 5!")
    private Integer quantityStars;

    @NotBlank(message = "Bình luận là một trường bắt buộc!")
    private String comment;
}
