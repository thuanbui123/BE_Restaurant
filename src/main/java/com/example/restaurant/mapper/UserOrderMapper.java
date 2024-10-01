package com.example.restaurant.mapper;

import com.example.restaurant.entity.ComboOrderEntity;
import com.example.restaurant.entity.FoodOrderedEntity;
import com.example.restaurant.response.ComboOrderedDetailResponse;
import com.example.restaurant.response.FoodOrderedDetailResponse;
import com.example.restaurant.response.UserOrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserOrderMapper {
    public static UserOrderResponse mapToResponse (List<ComboOrderEntity> comboOrders, List<FoodOrderedEntity> foodOrders) {
        UserOrderResponse response = new UserOrderResponse();
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
