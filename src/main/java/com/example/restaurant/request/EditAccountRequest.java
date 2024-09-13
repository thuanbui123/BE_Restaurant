package com.example.restaurant.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditAccountRequest {

    @NotBlank(message = "Email là một trường bắt buộc!")
    @Email(message = "Email không hợp lệ!")
    private String email;

    private String img = "http://localhost:8080/api-restaurant/uploads/73508276-6443-4464-8119-820c9c7d971f_avatarDefault.jpg";
}
