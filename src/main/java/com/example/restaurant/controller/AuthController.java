package com.example.restaurant.controller;

import com.example.restaurant.request.AccountRequest;
import com.example.restaurant.request.AuthRequest;
import com.example.restaurant.request.RegisterRequest;
import com.example.restaurant.response.ErrorResponse;
import com.example.restaurant.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AccountService service;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken (@Valid @RequestBody AuthRequest authRequest, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }

        return service.login(authRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.register(registerRequest);
    }
}
