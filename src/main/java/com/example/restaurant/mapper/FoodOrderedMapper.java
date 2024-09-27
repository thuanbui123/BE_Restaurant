package com.example.restaurant.mapper;

import com.example.restaurant.entity.EmbeddableId.FoodOrderedId;
import com.example.restaurant.entity.FoodOrderedEntity;
import com.example.restaurant.entity.FoodsEntity;
import com.example.restaurant.request.FoodOrderedDetailRequest;
import com.example.restaurant.request.FoodOrderedRequest;
import com.example.restaurant.response.FoodOrderedDetailResponse;
import com.example.restaurant.response.FoodOrderedResponse;
import com.example.restaurant.service.BillService;
import com.example.restaurant.service.FoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FoodOrderedMapper {
    private static FoodsService foodsService;
    private static BillService billService;

    @Autowired
    FoodOrderedMapper (FoodsService foodsService, BillService billService) {
        FoodOrderedMapper.foodsService = foodsService;
        FoodOrderedMapper.billService = billService;
    }

    public static List<FoodOrderedEntity> mapToEntity (FoodOrderedRequest request) {
        List<FoodOrderedDetailRequest> requests = request.getDetailRequests();
        return requests.stream()
                .map(request1 -> {
                    if (!billService.existsById(request.getBillId())) {
                        throw new IllegalArgumentException("Bill có id: " + request.getBillId() + " không tồn tại!");
                    }
                    if (!foodsService.existsById(request1.getFoodId())) {
                        throw new IllegalArgumentException("Món ăn có id: " + request1.getFoodId() + " không tồn tại!");
                    }
                    FoodOrderedEntity entity = new FoodOrderedEntity();
                    FoodsEntity foodsEntity = foodsService.findOneById(request1.getFoodId());
                    entity.setBill(billService.findOneById(request.getBillId()));
                    FoodOrderedId id = new FoodOrderedId(request.getBillId(), request1.getFoodId());
                    entity.setId(id);
                    entity.setFood(foodsEntity);
                    entity.setQuantity(request1.getQuantity());
                    entity.setTotalPrice(request1.getQuantity() * foodsEntity.getPrice());
                    return entity;
                })
                .toList();
    }

    public static FoodOrderedResponse mapToResponse (List<FoodOrderedEntity> entities){
        FoodOrderedResponse response = new FoodOrderedResponse();
        List<FoodOrderedDetailResponse> detailResponses = entities.stream()
                .map(entity -> {
                    FoodOrderedDetailResponse detailResponse = new FoodOrderedDetailResponse();
                    detailResponse.setFoodId(entity.getFood().getId());
                    detailResponse.setFoodName(entity.getFood().getName());
                    detailResponse.setQuantity(entity.getQuantity());
                    detailResponse.setTotalPrice(entity.getTotalPrice());
                    return detailResponse;
                })
                .toList();
        response.setResponses(detailResponses);
        return response;
    }
}
