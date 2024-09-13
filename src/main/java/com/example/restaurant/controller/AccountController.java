package com.example.restaurant.controller;

import com.example.restaurant.request.AccountRequest;
import com.example.restaurant.request.EditAccountRequest;
import com.example.restaurant.response.ErrorResponse;
import com.example.restaurant.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix, @RequestParam Integer page,  @RequestParam Integer size, @RequestParam(required = false) String query) {
        return service.findData(page, size, prefix, query);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
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

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @PutMapping("/update/{prefix}")
    public ResponseEntity<?> updateAccount (@PathVariable String prefix, @Valid @RequestBody EditAccountRequest accountRequest, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.updateData(prefix, accountRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @DeleteMapping("/delete/{prefix}")
    public ResponseEntity<?> deleteAccount (@PathVariable String prefix) {
        return service.deleteData(prefix);
    }
}
