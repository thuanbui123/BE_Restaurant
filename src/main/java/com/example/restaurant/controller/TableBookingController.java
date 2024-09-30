package com.example.restaurant.controller;

import com.example.restaurant.request.TableBookingRequest;
import com.example.restaurant.response.ErrorResponse;
import com.example.restaurant.service.TableBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/table-booking")
public class TableBookingController {
    @Autowired
    private TableBookingService service;

    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix, @RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer size, @RequestParam(required = false) String query,
                                       @RequestParam(required = false) Integer id) {
        return service.findData(prefix, page, size, query, id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @PostMapping("/add")
    public ResponseEntity<?> addData (@Valid @RequestBody TableBookingRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.addData(request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @PutMapping("/cancel-table-booking/{id}")
    public ResponseEntity<?> cancelTableBooking (@PathVariable Integer id, @Valid @RequestBody TableBookingRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errors));
        }
        return service.cancelData(id, request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE')")
    @PutMapping("/check-in-reservation/{tableBookingId}")
    public ResponseEntity<?> checkInReservation (@PathVariable Integer tableBookingId, @RequestParam(name = "table-id") Integer tableId) {
        return service.checkInReservation(tableBookingId, tableId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE')")
    @PutMapping("/change-table/{tableBookingId}")
    public ResponseEntity<?> changeTable (@PathVariable Integer tableBookingId,
                                          @RequestParam(name = "new-table-id") Integer newTableId) {
        return service.changeTable(newTableId, tableBookingId);
    }
}
