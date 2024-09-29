package com.example.restaurant.controller;

import com.example.restaurant.request.CustomerFoodReviewRequest;
import com.example.restaurant.service.CustomerFoodReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/food-reviews")
public class CustomerFoodReviewController {
    @Autowired
    private CustomerFoodReviewService service;

    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix, @RequestParam Integer page, @RequestParam Integer size, @RequestParam Integer foodId) {
        return service.findData(prefix, page, size, foodId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @PostMapping("/post")
    public ResponseEntity<?> addData (@Valid @RequestBody CustomerFoodReviewRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }
        return service.addData(request);
    }
}
