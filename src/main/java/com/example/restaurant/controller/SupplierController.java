package com.example.restaurant.controller;

import com.example.restaurant.request.SupplierRequest;
import com.example.restaurant.response.ErrorResponse;
import com.example.restaurant.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    @Autowired
    private SupplierService service;

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix, @RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) String query) {
        return service.findData(prefix, page, size, query);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @PostMapping("/add")
    public ResponseEntity<?> addData (@Valid @RequestBody SupplierRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.add(request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @PutMapping("/update/{code}")
    public ResponseEntity<?> updateData (@PathVariable String code, @Valid @RequestBody SupplierRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.updateData(code, request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @DeleteMapping("/delete/{code}")
    public ResponseEntity<?> deleteData (@PathVariable String code) {
        return service.deleteData(code);
    }
}
