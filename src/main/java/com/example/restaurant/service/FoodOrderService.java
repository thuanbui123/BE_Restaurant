package com.example.restaurant.service;

import com.example.restaurant.entity.EmbeddableId.FoodOrderedId;
import com.example.restaurant.entity.FoodOrderedEntity;
import com.example.restaurant.mapper.FoodOrderedMapper;
import com.example.restaurant.repository.FoodOrderedRepository;
import com.example.restaurant.request.FoodOrderedDetailRequest;
import com.example.restaurant.request.FoodOrderedRequest;
import com.example.restaurant.response.FoodOrderedResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodOrderService {
    @Autowired
    private FoodOrderedRepository repository;

    private FoodOrderedResponse findByBillId (Integer orderId) {
        List<FoodOrderedEntity> entities = repository.findByOrderedId(orderId);
        return (entities != null && !entities.isEmpty()) ? FoodOrderedMapper.mapToResponse(entities) : null;
    }

    public ResponseEntity<?> findData (String prefix, Integer orderId) {
        if (prefix.equals("search") && orderId != null) {
            return ResponseEntity.ok().body(findByBillId(orderId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
        }
    }

    @Transactional
    public ResponseEntity<?> addData (FoodOrderedRequest request) {
        try {
            for (FoodOrderedDetailRequest foodRequest : request.getDetailRequests()) {
                if (repository.existsById(new FoodOrderedId(request.getOrderedId(), foodRequest.getFoodId()))) {
                    return ResponseEntity.badRequest().body("Món ăn có mã: " + foodRequest.getFoodId() + " đã tồn tại trong đơn hàng có mã: " + request.getOrderedId());
                }
            }
            List<FoodOrderedEntity> entities = FoodOrderedMapper.mapToEntity(request);

            for (FoodOrderedEntity entity : entities) {
                if (!entity.getOrdered().getStatus().equalsIgnoreCase("Chờ xử lý")) {
                    return ResponseEntity.badRequest().body("Không được thêm món ăn vào đơn hàng có trạng thái đã thanh toán hoặc đã hủy!");
                }
                repository.save(entity);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm món ăn vào đơn hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (FoodOrderedRequest request) {
        try {
            for (FoodOrderedDetailRequest foodRequest : request.getDetailRequests()) {
                if (!repository.existsById(new FoodOrderedId(request.getOrderedId(), foodRequest.getFoodId()))) {
                    return ResponseEntity.badRequest().body("Món ăn có mã: " + foodRequest.getFoodId() + " không tồn tại trong đơn hàng có mã: " + request.getOrderedId());
                }
            }
            List<FoodOrderedEntity> entities = FoodOrderedMapper.mapToEntity(request);

            for (FoodOrderedEntity entity : entities) {
                if (!entity.getOrdered().getStatus().equalsIgnoreCase("Chờ xử lý")) {
                    return ResponseEntity.badRequest().body("Không được xóa món ăn khỏi đơn hàng có trạng thái đã thanh toán hoặc đã hủy!");
                }
                repository.deleteById(entity.getId());
            }

            return ResponseEntity.status(HttpStatus.OK).body("Xóa món ăn vào đơn hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
