package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ImportInvoiceRequest {
    @NotBlank(message = "Mã phiếu nhập nguyên liệu là một trường bắt buộc!")
    @Pattern(regexp = "^[^\\s]{6,10}$", message = "Mã phiếu nhập nguyên liệu phải dài từ 6 đến 10 ký tự và không chứa khoảng trắng!")
    private String code;
    @NotBlank(message = "Ngày nhập nguyên liệu là trường bắt buộc!")
    private String entryDate;
    @NotNull(message = "Mã nhân viên là trường bắt buộc!")
    @Min(value = 1, message = "Mã nhân viên phải lớn hơn 0!")
    private Integer employeeId;
}
