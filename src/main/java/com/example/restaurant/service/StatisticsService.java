package com.example.restaurant.service;

import com.example.restaurant.repository.AccountInfoRepository;
import com.example.restaurant.repository.BillRepository;
import com.example.restaurant.repository.ComboOrderRepository;
import com.example.restaurant.repository.FoodOrderedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class StatisticsService {
    @Autowired
    private AccountInfoRepository accountInfoRepository;

    @Autowired
    private FoodOrderedRepository foodOrderedRepository;

    @Autowired
    private ComboOrderRepository comboOrderRepository;

    @Autowired
    private BillRepository billRepository;

    public Long getNewAccountsByRoleUserForWeek(LocalDateTime startOfWeek, LocalDateTime endOfWeek) {
        return accountInfoRepository.countNewAccountsByRoleUserBetweenDates(startOfWeek, endOfWeek);
    }

    public Long getNewAccountsByRoleUserForMonth(LocalDateTime startOfMonth, LocalDateTime endOfMonth) {
        return accountInfoRepository.countNewAccountsByRoleUserBetweenDates(startOfMonth, endOfMonth);
    }

    public Map<String, Long> getNewAccountsByRoleUserForYear (int year) {
        Map<String, Long> accountsByMonth = new LinkedHashMap<>();
        for (int month = 1; month <= 12; month++) {
            LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
            Long count = accountInfoRepository.countNewAccountsByRoleUserBetweenDates(startOfMonth, endOfMonth);
            accountsByMonth.put(Month.of(month).name(), count);
        }
        return accountsByMonth;
    }

    // Lấy tuần hiện tại
    public Long getNewAccountsForCurrentWeek() {
        LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX);
        return getNewAccountsByRoleUserForWeek(startOfWeek, endOfWeek);
    }

    // Lấy tháng hiện tại
    public Long getNewAccountsForCurrentMonth() {
        LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
        return getNewAccountsByRoleUserForMonth(startOfMonth, endOfMonth);
    }

//    Tính doanh thu theo ngày
    public Long getRevenueForDay (LocalDate date) {
        LocalDateTime startOfDate = date.atStartOfDay();
        LocalDateTime endOfDate = startOfDate.withHour(23).withMinute(59).withSecond(59);
        return billRepository.sumTotalRevenueBetweenDates(startOfDate, endOfDate);
    }

//    Tính doanh thu theo tuần
    public Long getRevenueForWeek (int year, int weekNumber) {
        LocalDate startOfWeek = LocalDate.now().withYear(year).with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, weekNumber).with(DayOfWeek.MONDAY);
        LocalDateTime startOfWeekDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endOfWeekDateTime = startOfWeekDateTime.plusDays(6).withHour(23).withMinute(59).withSecond(59);
        return billRepository.sumTotalRevenueBetweenDates(startOfWeekDateTime, endOfWeekDateTime);
    }

//    Tính doanh thu theo tháng
    public Long getRevenueForMonth (int year, int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
        return billRepository.sumTotalRevenueBetweenDates(startOfMonth, endOfMonth);
    }

//    Thống kê doanh thu 12 tháng của một năm
    public Map<String, Long> getRevenueByMonthForYear(int year) {
        Map<String, Long> revenueByMonth = new LinkedHashMap<>();

        for (int month = 1; month <=12; month++) {
            LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);

            Long revenue = billRepository.sumTotalRevenueBetweenDates(startOfMonth, endOfMonth);
            revenueByMonth.put(Month.of(month).name(), revenue != null ? revenue : 0);
        }

        return revenueByMonth;
    }

//    Lấy các món ăn được bàn chiều nhất
    public List<Map<String, Object>> getTopSellingFoodsForDay(LocalDateTime date, int limit) {
        LocalDateTime startOfDay = date.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = date.withHour(23).withMinute(59).withSecond(59);
        return convertToResponse(foodOrderedRepository.findTopSellingFoods(startOfDay, endOfDay, limit));
    }

    // Lấy các món ăn được mua nhiều nhất theo tháng
    public List<Map<String, Object>> getTopSellingFoodsForMonth(int year, int month, int limit) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
        return convertToResponse(foodOrderedRepository.findTopSellingFoods(startOfMonth, endOfMonth, limit));
    }

    // Lấy các món ăn được mua nhiều nhất theo năm
    public List<Map<String, Object>> getTopSellingFoodsForYear(int year, int limit) {
        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endOfYear = startOfYear.with(TemporalAdjusters.lastDayOfYear()).withHour(23).withMinute(59).withSecond(59);
        return convertToResponse(foodOrderedRepository.findTopSellingFoods(startOfYear, endOfYear, limit));
    }

    //    Lấy các combo món ăn được bàn chiều nhất
    public List<Map<String, Object>> getTopSellingCombosForDay(LocalDateTime date, int limit) {
        LocalDateTime startOfDay = date.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = date.withHour(23).withMinute(59).withSecond(59);
        return convertToResponse(comboOrderRepository.findTopSellingCombos(startOfDay, endOfDay, limit));
    }

    // Lấy các combo món ăn được mua nhiều nhất theo tháng
    public List<Map<String, Object>> getTopSellingCombosForMonth(int year, int month, int limit) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
        return convertToResponse(comboOrderRepository.findTopSellingCombos(startOfMonth, endOfMonth, limit));
    }

    // Lấy các combos món ăn được mua nhiều nhất theo năm
    public List<Map<String, Object>> getTopSellingCombosForYear(int year, int limit) {
        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endOfYear = startOfYear.with(TemporalAdjusters.lastDayOfYear()).withHour(23).withMinute(59).withSecond(59);
        return convertToResponse(comboOrderRepository.findTopSellingCombos(startOfYear, endOfYear, limit));
    }

    private List<Map<String, Object>> convertToResponse(List<Object[]> results) {
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> foodData = new HashMap<>();
            foodData.put("foodName", result[0]);
            foodData.put("totalQuantity", result[1]);
            response.add(foodData);
        }
        return response;
    }
}
