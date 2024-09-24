package com.example.restaurant.controller;

import com.example.restaurant.request.IngredientCategoryLinkRequest;
import com.example.restaurant.service.IngredientCategoryLinkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("ingredient-category-link")
public class IngredientCategoryLinkController {
    @Autowired
    private IngredientCategoryLinkService service;

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix, @RequestParam Integer categoryId) {
        return service.findData(prefix, categoryId);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addData (@Valid @RequestBody IngredientCategoryLinkRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }
        return service.addData(request);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteData (@Valid @RequestBody IngredientCategoryLinkRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }
        return service.deleteData(request);
    }
}
