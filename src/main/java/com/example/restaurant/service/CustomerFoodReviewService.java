package com.example.restaurant.service;

import com.example.restaurant.entity.CustomerFoodReviewEntity;
import com.example.restaurant.mapper.CustomerFoodReviewMapper;
import com.example.restaurant.repository.CustomerFoodReviewRepository;
import com.example.restaurant.request.CustomerFoodReviewRequest;
import com.example.restaurant.utils.PaginateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerFoodReviewService {
    @Autowired
    private CustomerFoodReviewRepository repository;

    private ResponseEntity<?> findByFoodId (Integer foodId, Pageable pageable) {
        return PaginateUtil.paginate(
                pg -> repository.findByFoodId(foodId, pageable),
                pageable,
                CustomerFoodReviewMapper::mapToResponse
        );
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, Integer foodId) {
        if (prefix.equals("find-by-food-id") && foodId != null) {
            Pageable pageable = PageRequest.of(page, size);
            return findByFoodId(foodId, pageable);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tai!");
    }

    public ResponseEntity<?> addData (CustomerFoodReviewRequest request) {
        try {
            CustomerFoodReviewEntity entity = CustomerFoodReviewMapper.mapToEntity(request);
            repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Đăng bình luận món ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
