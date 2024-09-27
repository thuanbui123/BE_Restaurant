package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BillRequest {
    @NotBlank(message = "Mã bài viết là một trường bắt buộc!")
    @Pattern(regexp = "^[^\\s]{6,10}$", message = "Mã bài viết phải dài từ 6 đến 10 ký tự và không chứa khoảng trắng!")
    private String code;

    @NotNull(message = "Mã nhân viên là một trường bắt buộc!")
    @Min(value = 1, message = "Mã nhân viên phải lớn hơn 0!")
    private Integer employeeId;

    @NotNull(message = "Mã khách hàng là một trường bắt buộc!")
    @Min(value = 1, message = "Mã khách hàng phải lớn hơn 0!")
    private Integer customerId;

    private String status = "Chờ xử lý";

    private String note;
}
