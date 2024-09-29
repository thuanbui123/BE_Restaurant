package com.example.restaurant.mapper;

import com.example.restaurant.entity.CustomerFoodReviewEntity;
import com.example.restaurant.entity.CustomersEntity;
import com.example.restaurant.entity.FoodsEntity;
import com.example.restaurant.repository.CustomersRepository;
import com.example.restaurant.repository.FoodsRepository;
import com.example.restaurant.request.CustomerFoodReviewRequest;
import com.example.restaurant.response.CustomerFoodReviewResponse;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerFoodReviewMapper {
    private static CustomersRepository customersRepository;
    private static FoodsRepository foodsRepository;

    @Autowired
    CustomerFoodReviewMapper(CustomersRepository customersRepository, FoodsRepository foodsRepository) {
        CustomerFoodReviewMapper.customersRepository = customersRepository;
        CustomerFoodReviewMapper.foodsRepository = foodsRepository;
    }

    public static CustomerFoodReviewEntity mapToEntity (CustomerFoodReviewRequest request) {
        if (!customersRepository.existsById(request.getCustomerId())) {
            throw new IllegalArgumentException("Không tồn tại khách hàng có mã: " + request.getCustomerId() + "!");
        }
        if (!foodsRepository.existsById(request.getFoodId())) {
            throw new IllegalArgumentException("Không tồn tại món ăn có mã: " + request.getFoodId() + "!");
        }
        CustomersEntity customersEntity = customersRepository.findOneById(request.getCustomerId());
        FoodsEntity foods = foodsRepository.findOneById(request.getFoodId());
        CustomerFoodReviewEntity entity = new CustomerFoodReviewEntity();
        entity.setQuantityStars(request.getQuantityStars());
        entity.setComment(request.getComment());
        entity.setCustomers(customersEntity);
        entity.setFoods(foods);
        return entity;
    }

    public static CustomerFoodReviewResponse mapToResponse (CustomerFoodReviewEntity entity) {
        CustomerFoodReviewResponse response = new CustomerFoodReviewResponse();
        response.setAvatar(entity.getCustomers().getAccount().getImg());
        response.setCustomerName(entity.getCustomers().getName());
        response.setQuantityStars(entity.getQuantityStars());
        response.setComment(entity.getComment());
        response.setPostDate(TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt()));
        return response;
    }
}
