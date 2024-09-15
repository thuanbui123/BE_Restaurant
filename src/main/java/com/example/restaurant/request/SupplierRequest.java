package com.example.restaurant.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SupplierRequest {
    @NotBlank(message = "Mã nhà cung cấp là một trường dữ liệu bắt buộc!")
    @Pattern(regexp = "^[^\\s]{6,10}$", message = "Mã nhà cung cấp phải dài từ 6 đến 10 ký tự và không chứa khoảng trắng!")
    private String code;

    @NotBlank(message = "Tên nhà cung cấp là một trường dữ liệu bắt buộc!")
    private String name;

    @NotBlank(message = "Số điện thoại là một trường dữ liệu bắt buộc!")
    private String phoneNumber;

    @NotBlank(message = "Email là một trường dữ liệu bắt buộc!")
    @Email(message = "Không đúng định dạng email!")
    private String email;

    @NotBlank(message = "Địa chỉ là một trường dữ liệu bắt buộc!")
    private String address;

    @NotBlank(message = "Mô tả nhà cung cấp là trường bắt buộc!")
    private String description;
}
