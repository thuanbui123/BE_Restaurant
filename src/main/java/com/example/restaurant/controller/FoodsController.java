package com.example.restaurant.controller;

import com.example.restaurant.request.FoodsRequest;
import com.example.restaurant.response.ErrorResponse;
import com.example.restaurant.service.FoodsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/foods")
public class FoodsController {
    @Autowired
    private FoodsService service;

    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix, @RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer size, @RequestParam(required = false) String query,
                                       @RequestParam(required = false, name = "limit-food") Integer limitFood,
                                       @RequestParam(required = false, name = "account-id") Integer id) {
        return service.findData(prefix, page, size, query, id, limitFood);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addData (@Valid @RequestBody FoodsRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.addFood(request);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @PutMapping("/update/{code}")
    public ResponseEntity<?> updateData (@PathVariable String code, @Valid @RequestBody FoodsRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.updateFood(code, request);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @DeleteMapping("/delete/{code}")
    public ResponseEntity<?> deleteData (@PathVariable String code) {
        return service.deleteFood(code);
    }
}
