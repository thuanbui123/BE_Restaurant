package com.example.restaurant.mapper;

import com.example.restaurant.entity.EmbeddableId.FoodOrderedId;
import com.example.restaurant.entity.FoodOrderedEntity;
import com.example.restaurant.entity.FoodsEntity;
import com.example.restaurant.repository.OrderedRepository;
import com.example.restaurant.request.FoodOrderedDetailRequest;
import com.example.restaurant.request.FoodOrderedRequest;
import com.example.restaurant.response.FoodOrderedDetailResponse;
import com.example.restaurant.response.FoodOrderedResponse;
import com.example.restaurant.service.FoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FoodOrderedMapper {
    private static FoodsService foodsService;
    private static OrderedRepository orderedRepository;

    @Autowired
    FoodOrderedMapper (FoodsService foodsService, OrderedRepository orderedRepository) {
        FoodOrderedMapper.foodsService = foodsService;
        FoodOrderedMapper.orderedRepository = orderedRepository;
    }

    public static List<FoodOrderedEntity> mapToEntity (FoodOrderedRequest request) {
        List<FoodOrderedDetailRequest> requests = request.getDetailRequests();
        return requests.stream()
                .map(request1 -> {
                    if (!orderedRepository.existsById(request.getOrderedId())) {
                        throw new IllegalArgumentException("Hóa đơn có id: " + request.getOrderedId() + " không tồn tại!");
                    }
                    if (!foodsService.existsById(request1.getFoodId())) {
                        throw new IllegalArgumentException("Món ăn có id: " + request1.getFoodId() + " không tồn tại!");
                    }
                    FoodOrderedEntity entity = new FoodOrderedEntity();
                    FoodsEntity foodsEntity = foodsService.findOneById(request1.getFoodId());
                    entity.setOrdered(orderedRepository.findOneById(request.getOrderedId()));
                    FoodOrderedId id = new FoodOrderedId(request.getOrderedId(), request1.getFoodId());
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
