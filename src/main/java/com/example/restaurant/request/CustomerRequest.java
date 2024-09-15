package com.example.restaurant.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotBlank(message = "Mã khách hàng là một trường dữ liệu bắt buộc!")
    @Pattern(regexp = "^[^\\s]{6,10}$", message = "Mã khách hàng phải dài từ 6 đến 10 ký tự và không chứa khoảng trắng!")
    private String code;

    @NotBlank(message = "Tên khách hàng là một trường dữ liệu bắt buộc!")
    private String name;

    @NotBlank(message = "Số điện thoại là một trường dữ liệu bắt buộc!")
    private String phoneNumber;

    @NotBlank(message = "Email là một trường dữ liệu bắt buộc!")
    @Email(message = "Không đúng định dạng email!")
    private String email;

    @NotBlank(message = "Địa chỉ là một trường dữ liệu bắt buộc!")
    private String address;

    @NotNull(message = "Mã tài khoản không được để trống!")
    @Min(value = 1, message = "Mã tài khoản phải lớn hơn 0!")
    private Integer accountId;
}
