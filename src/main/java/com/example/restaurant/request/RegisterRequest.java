package com.example.restaurant.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email là một trường bắt buộc!")
    @Email(message = "Email không hợp lệ!")
    private String email;

    @NotBlank(message = "Tên đăng nhập là một trường bắt buộc!")
    private String username;

    @NotBlank(message = "Mật khẩu là một trường bắt buộc!")
    @Size(min = 8, message = "Mật khẩu phải dài ít nhất 8 ký tự!")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Mật khẩu phải chứa ít nhất một chữ số, một chữ cái thường, một chữ cái viết hoa và một ký tự đặc biệt!")
    private String password;
}
