package com.example.restaurant.controller;

import com.example.restaurant.DTO.AuthRequest;
import com.example.restaurant.DTO.RegisterRequest;
import com.example.restaurant.entity.AccountInfo;
import com.example.restaurant.response.ErrorResponse;
import com.example.restaurant.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AccountController {
    @Autowired
    private AccountService service;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/add-new-user")
    public String addNewUser (@RequestBody AccountInfo accountInfo) {
        return service.addUser(accountInfo);
    }

    @GetMapping("/user/user-profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile () {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/admin-profile")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String adminProfile () {
        return "Welcome to Admin Profile";
    }

//    @PostMapping("/generate-token")
//    public String authenticateAndGetToken (@RequestBody AuthRequest authRequest) {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//        if (authentication.isAuthenticated()) {
//            return jwtService.generateToken(authRequest.getUsername());
//        } else {
//            throw new UsernameNotFoundException("Invalid user request!");
//        }
//    }

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
