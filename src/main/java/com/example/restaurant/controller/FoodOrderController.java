package com.example.restaurant.controller;

import com.example.restaurant.request.FoodOrderedRequest;
import com.example.restaurant.response.ErrorResponse;
import com.example.restaurant.service.FoodOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/food-order")
public class FoodOrderController {
    @Autowired
    private FoodOrderService service;

    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix, @RequestParam("bill-id") Integer id) {
        return service.findData(prefix, id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @PostMapping("/add")
    public ResponseEntity<?> addData (@Valid @RequestBody FoodOrderedRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.addData(request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteData (@Valid @RequestBody FoodOrderedRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.deleteData(request);
    }
}
