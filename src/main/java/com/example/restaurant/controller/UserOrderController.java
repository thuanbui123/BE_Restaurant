package com.example.restaurant.controller;

import com.example.restaurant.request.CancelOrderRequest;
import com.example.restaurant.request.UserOrderRequest;
import com.example.restaurant.service.UserOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-order")
public class UserOrderController {
    @Autowired
    private UserOrderService service;

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @GetMapping(value = "/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix, @RequestParam(name = "customer-id") Integer customerId, @RequestParam String status) {
        return service.findData(prefix, customerId, status);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @PostMapping(value = "/ordered")
    public ResponseEntity<?> ordered (@Valid @RequestBody UserOrderRequest request, @RequestParam(value = "customer-id") Integer customerId, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }
        return service.userOrder(customerId, request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @PutMapping(value = "/update-ordered")
    public ResponseEntity<?> updateOrdered (@RequestBody UserOrderRequest request, @RequestParam(value = "order-id") Integer orderId) {
        return service.updateOrder(orderId, request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @DeleteMapping(value = "/delete-item-in-order")
    public ResponseEntity<?> deleteItemInOrdered (@RequestBody UserOrderRequest request, @RequestParam(value = "order-id") Integer orderId) {
        return service.deleteItemInOrder(orderId, request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @PostMapping(value = "/order-payment")
    public ResponseEntity<?> orderPayment (@RequestParam(value = "order-id") Integer orderId) {
        return service.orderPayment(orderId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @PutMapping(value = "/cancel-order")
    public ResponseEntity<?> cancelOrder (@RequestParam(value = "order-id") Integer orderId, @Valid @RequestBody CancelOrderRequest note) {
        return service.cancelOrder(orderId, note);
    }
}
