package com.example.restaurant.controller;

import com.example.restaurant.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService service;

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @GetMapping("/new-users/weekly")
    public ResponseEntity<Long> getNewUserAccountsForWeek() {
        Long count = service.getNewAccountsForCurrentWeek();
        return ResponseEntity.ok(count);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @GetMapping("/new-users/monthly")
    public ResponseEntity<Long> getNewUserAccountsForMonth() {
        Long count = service.getNewAccountsForCurrentMonth();
        return ResponseEntity.ok(count);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @GetMapping("/new-accounts/year")
    public ResponseEntity<Map<String, Long>> getNewAccountsForYear(@RequestParam int year) {
        Map<String, Long> newAccountsByMonth = service.getNewAccountsByRoleUserForYear(year);
        return ResponseEntity.ok(newAccountsByMonth);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @GetMapping("/revenue/year/{year}")
    public ResponseEntity<?> getRevenueByYear (@PathVariable Integer year) {
        return ResponseEntity.ok(service.getRevenueByMonthForYear(year));
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @GetMapping("/revenue/week")
    public ResponseEntity<?> getRevenueByYear (@RequestParam(name = "year") int year, @RequestParam(name = "week") int weekNumber) {
        return ResponseEntity.ok(service.getRevenueForWeek(year, weekNumber));
    }

    @GetMapping("/revenue/month")
    public Long getRevenueForMonth(@RequestParam int year, @RequestParam int month) {
        return service.getRevenueForMonth(year, month);
    }

    // API lấy món ăn bán chạy nhất theo ngày'
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @GetMapping("/top-selling/foods/day")
    public ResponseEntity<?> getTopSellingFoodsForDay(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                      @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return ResponseEntity.ok(service.getTopSellingFoodsForDay(date.atStartOfDay(), limit));
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @GetMapping("/top-selling/foods/month")
    public ResponseEntity<?> getTopSellingFoodsForMonth(@RequestParam("year") int year,
                                                        @RequestParam("month") int month,
                                                        @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return ResponseEntity.ok(service.getTopSellingFoodsForMonth(year, month, limit));
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @GetMapping("/top-selling/foods/year")
    public ResponseEntity<?> getTopSellingFoodsForYear(@RequestParam("year") int year,
                                                       @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return ResponseEntity.ok(service.getTopSellingFoodsForYear(year, limit));
    }

    // API lấy combo món ăn bán chạy nhất theo ngày'
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @GetMapping("/top-selling/combos/day")
    public ResponseEntity<?> getTopSellingCombosForDay(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                      @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return ResponseEntity.ok(service.getTopSellingCombosForDay(date.atStartOfDay(), limit));
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @GetMapping("/top-selling/combos/month")
    public ResponseEntity<?> getTopSellingCombosForMonth(@RequestParam("year") int year,
                                                         @RequestParam("month") int month,
                                                         @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return ResponseEntity.ok(service.getTopSellingCombosForMonth(year, month, limit));
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ADMIN')")
    @GetMapping("/top-selling/combos/year")
    public ResponseEntity<?> getTopSellingCombosForYear(@RequestParam("year") int year,
                                                       @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return ResponseEntity.ok(service.getTopSellingCombosForYear(year, limit));
    }
}
