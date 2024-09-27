package com.example.restaurant.mapper;

import com.example.restaurant.entity.ComboFoodEntity;
import com.example.restaurant.entity.EmbeddableId.ComboFoodId;
import com.example.restaurant.entity.FoodsEntity;
import com.example.restaurant.repository.ComboFoodRepository;
import com.example.restaurant.request.ComboFoodDetailRequest;
import com.example.restaurant.request.ComboFoodRequest;
import com.example.restaurant.response.ComboFoodDetailResponse;
import com.example.restaurant.response.ComboFoodResponse;
import com.example.restaurant.response.ComboResponse;
import com.example.restaurant.service.ComboService;
import com.example.restaurant.service.FoodsService;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ComboFoodMapper {
    private static ComboService comboService;

    private static FoodsService foodsService;

    private static ComboFoodRepository comboFoodRepository;

    @Autowired
    ComboFoodMapper (ComboService comboService, FoodsService foodsService, ComboFoodRepository comboFoodRepository) {
        ComboFoodMapper.comboService = comboService;
        ComboFoodMapper.foodsService = foodsService;
        ComboFoodMapper.comboFoodRepository = comboFoodRepository;
    }

    public static List<ComboFoodEntity> mapToEntity (ComboFoodRequest request) {
        List<ComboFoodDetailRequest> comboFoodDetailRequest = request.getDetailRequests();

        return comboFoodDetailRequest.stream()
                .map(combo -> {
                    if (comboFoodRepository.existsById(new ComboFoodId(request.getComboId(), combo.getFoodId()))) {
                        return null;
                    }
                    if(!comboService.existsById(request.getComboId())) {
                        throw new IllegalArgumentException("Combo món ăn có id: " + request.getComboId() + " không tồn tại!");
                    }

                    if (!foodsService.existsById(combo.getFoodId())) {
                        throw new IllegalArgumentException("Món ăn có id: " + combo.getFoodId() + " không tồn tại!");
                    }
                    ComboFoodEntity entity = new ComboFoodEntity();
                    entity.setCombo(comboService.findOneById(request.getComboId()));
                    FoodsEntity foods = foodsService.findOneById(combo.getFoodId());
                    entity.setId(new ComboFoodId(request.getComboId(), combo.getFoodId()));
                    entity.setFood(foods);
                    entity.setAmountOfFood(combo.getAmountOfFood());
                    entity.setTotalPrice(combo.getAmountOfFood() * foods.getPrice());
                    return entity;
                })
                .toList();
    }

    public static ComboResponse mapToResponse (ComboFoodEntity entity) {
        ComboResponse response = new ComboResponse();
        response.setId(entity.getCombo().getId());
        response.setCode(entity.getCombo().getCode());
        response.setName(entity.getCombo().getName());
        response.setStatus(entity.getCombo().getStatus());
        response.setDescription(entity.getCombo().getDescription());
        response.setImg(entity.getCombo().getImg());
        response.setPrice(entity.getTotalPrice());
        return response;
    }

    public static ComboFoodResponse mapToDetailResponse(List<ComboFoodEntity> entities) {
        ComboFoodResponse response = new ComboFoodResponse();
        response.setComboId(entities.get(0).getCombo().getId());
        response.setComboName(entities.get(0).getCombo().getName());
        response.setDescription(entities.get(0).getCombo().getDescription());
        response.setImg(entities.get(0).getCombo().getImg());
        response.setFoodDetailResponses(entities.stream()
                .map(entity -> {
                    ComboFoodDetailResponse comboFoodDetailResponse = new ComboFoodDetailResponse();
                    comboFoodDetailResponse.setId(entity.getFood().getId());
                    comboFoodDetailResponse.setCode(entity.getFood().getCode());
                    comboFoodDetailResponse.setImg(entity.getFood().getImg());
                    comboFoodDetailResponse.setName(entity.getFood().getName());
                    comboFoodDetailResponse.setPrice(entity.getFood().getPrice());
                    comboFoodDetailResponse.setDescription(entity.getFood().getDescription());
                    comboFoodDetailResponse.setAmountOfFood(entity.getAmountOfFood());
                    return comboFoodDetailResponse;
                })
                .toList()
        );
        response.setStatus(entities.get(0).getCombo().getStatus());

        response.setTotalPrice(entities.get(0).getTotalPrice());
        return response;
    }
}
