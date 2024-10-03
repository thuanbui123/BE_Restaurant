package com.example.restaurant.controller;

import com.example.restaurant.request.CancelOrderRequest;
import com.example.restaurant.request.EmployeeOrderRequest;
import com.example.restaurant.service.EmployeeOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee-order")
public class EmployeeOrderController {
    @Autowired
    private EmployeeOrderService service;

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @PostMapping("/call-order")
    public ResponseEntity<?> callOrder (@RequestParam(name = "table-id", required = false) Integer tableId,
                                        @RequestParam(name = "customer-id") Integer customerId,
                                        @RequestBody EmployeeOrderRequest request) {
        return service.callOrder(tableId, customerId, request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @PutMapping(value = "/update-ordered")
    public ResponseEntity<?> updateOrdered (@RequestBody EmployeeOrderRequest request, @RequestParam(value = "order-id") Integer orderId) {
        return service.updateOrder(orderId, request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @DeleteMapping(value = "/delete-item-in-order")
    public ResponseEntity<?> deleteItemInOrdered (@RequestBody EmployeeOrderRequest request, @RequestParam(value = "order-id") Integer orderId) {
        return service.deleteItemInOrder(orderId, request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @PostMapping(value = "/order-payment")
    public ResponseEntity<?> orderPayment (@RequestParam(value = "order-id") Integer orderId) {
        return service.orderPayment(orderId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE')")
    @PutMapping(value = "/cancel-order")
    public ResponseEntity<?> cancelOrder (@RequestParam(value = "order-id") Integer orderId, @Valid @RequestBody CancelOrderRequest note) {
        return service.cancelOrder(orderId, note);
    }
}
