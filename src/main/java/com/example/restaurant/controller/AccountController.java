package com.example.restaurant.controller;

import com.example.restaurant.request.AccountRequest;
import com.example.restaurant.request.EditAccountRequest;
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
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService service;

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix, @RequestParam(required = false) Integer page,  @RequestParam(required = false) Integer size, @RequestParam(required = false) String query) {
        return service.findData(page, size, prefix, query);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @PostMapping("/add")
    public ResponseEntity<?> addAccount(@Valid @RequestBody AccountRequest accountRequest, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.addAccount(accountRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @PutMapping("/update/{code}")
    public ResponseEntity<?> updateAccount (@PathVariable Integer code, @Valid @RequestBody EditAccountRequest accountRequest, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.updateData(code, accountRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @DeleteMapping("/delete/{prefix}")
    public ResponseEntity<?> deleteAccount (@PathVariable Integer prefix) {
        return service.deleteData(prefix);
    }
}
