package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogPostRequest {
    @NotBlank(message = "Mã bài viết là một trường bắt buộc!")
    private String code;
    @NotBlank(message = "Tiêu đề bài viết là một trường bắt buộc!")
    private String title;
    @NotBlank(message = "Nội dung bài viết là một trường bắt buộc!")
    private String content;
    @NotNull(message = "Mã nhân viên là một trường bắt buộc!")
    @Min(value = 1, message = "Mã nhân viên phải lớn hơn 0")
    private Integer employeeId;
}
