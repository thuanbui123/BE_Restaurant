package com.example.restaurant.mapper;

import com.example.restaurant.entity.BillEntity;
import com.example.restaurant.entity.ComboOrderEntity;
import com.example.restaurant.entity.FoodOrderedEntity;
import com.example.restaurant.entity.OrderedEntity;
import com.example.restaurant.response.ComboOrderedDetailResponse;
import com.example.restaurant.response.EmployeeOrderResponse;
import com.example.restaurant.response.FoodOrderedDetailResponse;
import com.example.restaurant.response.UserOrderResponse;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeOrderMapper {
    public static EmployeeOrderResponse mapToResponse (BillEntity bill, OrderedEntity ordered, List<ComboOrderEntity> comboOrders, List<FoodOrderedEntity> foodOrders) {
        EmployeeOrderResponse response = new EmployeeOrderResponse();
        response.setCode(bill.getCode());
        response.setDateOrder(TimeConvertUtil.convertTimestampToDate(ordered.getCreatedAt()));
        response.setCustomerCode(ordered.getCustomer().getCode());
        response.setCustomerName(ordered.getCustomer().getName());
        response.setNumberPhone(ordered.getCustomer().getPhoneNumber());
        response.setAddress(ordered.getCustomer().getAddress());
        response.setTotalPrice(bill.getTotalPrice());
        response.setDatePayment(TimeConvertUtil.convertTimestampToDate(bill.getCreatedAt()));
        if (ordered.getTables() != null && !ordered.getTables().isEmpty()) {
            response.setTableCode(ordered.getTables().get(0).getCode());
            response.setLocation(ordered.getTables().get(0).getLocation());
        }
        response.setComboOrdered(
                comboOrders.stream()
                        .map(entity -> {
                            ComboOrderedDetailResponse detailResponse = new ComboOrderedDetailResponse();
                            detailResponse.setComboId(entity.getCombo().getId());
                            detailResponse.setImg(entity.getCombo().getImg());
                            detailResponse.setQuantity(entity.getQuantity());
                            detailResponse.setComboName(entity.getCombo().getName());
                            detailResponse.setQuantity(entity.getQuantity());
                            detailResponse.setTotalPrice(entity.getTotalPrice());
                            return detailResponse;
                        })
                        .toList()
        );
        response.setFoodOrdered(
                foodOrders.stream()
                        .map(entity -> {
                            FoodOrderedDetailResponse detailResponse = new FoodOrderedDetailResponse();
                            detailResponse.setImg(entity.getFood().getImg());
                            detailResponse.setFoodId(entity.getFood().getId());
                            detailResponse.setFoodName(entity.getFood().getName());
                            detailResponse.setQuantity(entity.getQuantity());
                            detailResponse.setTotalPrice(entity.getTotalPrice());
                            return detailResponse;
                        })
                        .toList()
        );
        return response;
    }
}
