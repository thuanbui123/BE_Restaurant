package com.example.restaurant.service;

import com.example.restaurant.entity.*;
import com.example.restaurant.mapper.FoodsMapper;
import com.example.restaurant.model.FoodRatingModel;
import com.example.restaurant.repository.*;
import com.example.restaurant.response.FoodsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private CustomerFoodReviewRepository customerFoodReviewRepository;

    @Autowired
    private OrderedRepository orderedRepository;

    @Autowired
    private FoodOrderedRepository foodOrderedRepository;

    @Autowired
    private FoodsRepository foodsRepository;

    /**
     * Gợi ý món ăn cho một khách hàng dựa trên đánh giá của các khách hàng khác.
     */
    public List<FoodsResponse> recommendFoodsForCustomer (Integer customerId, Integer limitFoods) {
        // Lấy tất cả đánh giá của khách hàng hiện tại
        List<CustomerFoodReviewEntity> currentUserReview = customerFoodReviewRepository.findByCustomerId(customerId);

        if (currentUserReview == null || currentUserReview.isEmpty()) {
            return getDefaultRecommendations(limitFoods);
        }

        // Tìm tất cả các khách hàng khác
        List<Integer> otherCustomerIds = customerFoodReviewRepository.findAll().stream()
                .map(review -> review.getCustomers().getId())
                .distinct()
                .filter(id -> !id.equals(customerId))
                .toList();

        Map<Integer, Double> similarityScores = new HashMap<>();
        // Tính độ tương đồng giữa khách hàng hiện tại và các khách hàng khác
        for (Integer otherCustomerId : otherCustomerIds) {
            double similarity = calculateSimilarity(customerId, otherCustomerId);
            similarityScores.put(otherCustomerId, similarity);
        }

        // Sắp xếp theo độ tương đồng giảm dần
        List<Integer> similarCustomers = similarityScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double> comparingByKey().reversed())
                .limit(5) //Giới hạn số khách hàng tương tự
                .map(Map.Entry::getKey)
                .toList();
        Set<FoodsEntity> recommendedFoods = new HashSet<>();

        //Gợi ý món ăn từ các khách hàng tương tự
        for (Integer similarCustomerId : similarCustomers) {
            // Lấy danh sách các bill của khách hàng tương tự
            List<OrderedEntity> ordereds = orderedRepository.findByCustomerId(similarCustomerId);

            // Duyệt qua các món ăn đã được đặt trong các bill
            for (OrderedEntity ordered : ordereds) {
                List<FoodOrderedEntity> foodOrders = foodOrderedRepository.findByOrderedId(ordered.getId());
                for (FoodOrderedEntity foodOrdered : foodOrders) {
                    FoodsEntity food = foodsRepository.findById(foodOrdered.getFood().getId()).orElse(null);
                    if (food != null && !hasAlreadyOrdered(customerId, food.getId())) {
                        recommendedFoods.add(food);
                    }
                }
            }
        }

        List<FoodsResponse> responses = new ArrayList<>();
        for (FoodsEntity entity : recommendedFoods) {
            responses.add(FoodsMapper.mapToResponse(entity));
        }

        return responses.stream().limit(limitFoods).collect(Collectors.toList());
    }

    /**
     * Tính độ tương đồng giữa hai khách hàng dựa trên các đánh giá món ăn.
     */
    private double calculateSimilarity (Integer customerId1, Integer customerId2) {
        List<CustomerFoodReviewEntity> reviews1 = customerFoodReviewRepository.findByCustomerId(customerId1);
        List<CustomerFoodReviewEntity> reviews2 = customerFoodReviewRepository.findByCustomerId(customerId2);
        // Tìm các món ăn mà cả hai khách hàng đã đánh giá
        Set<Integer> commonFoodIds = reviews1.stream()
                .map(CustomerFoodReviewEntity::getFoodId)
                .collect(Collectors.toSet());
        commonFoodIds.retainAll(reviews2.stream()
                .map(CustomerFoodReviewEntity::getFoodId)
                .collect(Collectors.toSet()));
        if (commonFoodIds.isEmpty()) {
            return 0; // Nếu không có món ăn chung, độ tương đồng bằng 0
        }

        // Tính toán điểm tương đồng
        double sum = 0;
        for (Integer foodId : commonFoodIds) {
            double rating1 = reviews1.stream().filter(r -> r.getFoodId().equals(foodId)).findFirst().get().getQuantityStars();
            double rating2 = reviews2.stream().filter(r -> r.getFoodId().equals(foodId)).findFirst().get().getQuantityStars();

            sum += (rating1 * rating2);
        }

        return sum / commonFoodIds.size();
    }

    /**
     * Kiểm tra xem khách hàng đã đặt món ăn này chưa.
     */
    private boolean hasAlreadyOrdered(Integer customerId, Integer foodId) {
        // Lấy danh sách bill của khách hàng
        List<OrderedEntity> customerOrders = orderedRepository.findByCustomerId(customerId);
        return customerOrders.stream()
                .flatMap(bill -> foodOrderedRepository.findByOrderedId(bill.getId()).stream())
                .anyMatch(order -> order.getFood().getId().equals(foodId));
    }

    public List<FoodRatingModel> getTopFoodsByAverageRating(int limit) {
        List<Object[]> results = customerFoodReviewRepository.findTopFoodsByAverageRating(limit);
        return results.stream()
                .map(row -> new FoodRatingModel((Integer) row[0], (BigDecimal) row[1]))
                .collect(Collectors.toList());
    }


    public List<FoodsResponse> getDefaultRecommendations (Integer limit) {
        List<FoodRatingModel> topFoodRatings = getTopFoodsByAverageRating(limit);

        // Lấy thông tin món ăn từ foodsRepository dựa trên danh sách foodId từ rating
        List<FoodsEntity> foodsEntities = new ArrayList<>();

        for (FoodRatingModel ratingModel : topFoodRatings) {
            foodsRepository.findById(ratingModel.getFoodId()).ifPresent(foodsEntities::add);
        }

        List<FoodsResponse> responses = new ArrayList<>();
        for (FoodsEntity food : foodsEntities) {
            responses.add(FoodsMapper.mapToResponse(food));
        }

        return responses;
    }
}
