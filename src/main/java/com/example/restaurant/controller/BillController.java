package com.example.restaurant.controller;

import com.example.restaurant.request.BillRequest;
import com.example.restaurant.request.CancelBillRequest;
import com.example.restaurant.service.BillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/bills")
public class BillController {
    @Autowired
    private BillService service;

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) String query) {
        return service.findData(prefix, page, size, query);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @PostMapping("/add")
    public ResponseEntity<?> addData (@Valid @RequestBody BillRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }
        return service.addData(request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @PutMapping("/update/{code}")
    public ResponseEntity<?> updateData (@PathVariable String code, @Valid @RequestBody BillRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }
        return service.updateData(code, request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @PutMapping("/cancel/{code}")
    public ResponseEntity<?> updateData (@PathVariable String code, @Valid @RequestBody CancelBillRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }
        return service.cancelBill(code, request);
    }
}
