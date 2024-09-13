package com.example.restaurant.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "Tên đăng nhập là một trường bắt buộc!")
    private String username;

    @NotBlank(message = "Mật khẩu là một trường bắt buộc!")
    private String password;
}
