package com.example.restaurant.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditAccountRequest {
    private String img = "http://localhost:8080/api-restaurant/uploads/73508276-6443-4464-8119-820c9c7d971f_avatarDefault.jpg";
}
