package com.example.restaurant.controller;

import com.example.restaurant.request.ComboFoodRequest;
import com.example.restaurant.response.ErrorResponse;
import com.example.restaurant.service.ComboFoodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/combo-food")
public class ComboFoodController {
    @Autowired
    private ComboFoodService service;

    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false, name = "combo-id") Integer id) {
        return service.findData(prefix, page, size, id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @PostMapping("/add")
    public ResponseEntity<?> addData (@Valid @RequestBody ComboFoodRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.addData(request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteData(@RequestParam(name = "combo-id") Integer comboId, @RequestParam("food-id") Integer foodId) {
        return service.deleteData(comboId, foodId);
    }
}
