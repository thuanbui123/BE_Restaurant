package com.example.restaurant.service;

import com.example.restaurant.entity.BillEntity;
import com.example.restaurant.entity.EmbeddableId.FoodOrderedId;
import com.example.restaurant.entity.FoodOrderedEntity;
import com.example.restaurant.mapper.FoodOrderedMapper;
import com.example.restaurant.repository.BillRepository;
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
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FoodOrderService {
    @Autowired
    private FoodOrderedRepository repository;

    @Autowired
    private BillRepository billRepository;

    private FoodOrderedResponse findByBillId (Integer billId) {
        List<FoodOrderedEntity> entities = repository.findByBillId(billId);
        return (entities != null && !entities.isEmpty()) ? FoodOrderedMapper.mapToResponse(entities) : null;
    }

    public ResponseEntity<?> findData (String prefix, Integer billId) {
        if (prefix.equals("search") && billId != null) {
            return ResponseEntity.ok().body(findByBillId(billId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
        }
    }

    @Transactional
    public ResponseEntity<?> addData (FoodOrderedRequest request) {
        try {
            for (FoodOrderedDetailRequest foodRequest : request.getDetailRequests()) {
                if (repository.existsById(new FoodOrderedId(request.getBillId(), foodRequest.getFoodId()))) {
                    return ResponseEntity.badRequest().body("Món ăn có mã: " + foodRequest.getFoodId() + " đã tồn tại trong đơn hàng có mã: " + request.getBillId());
                }
            }
            List<FoodOrderedEntity> entities = FoodOrderedMapper.mapToEntity(request);

            AtomicLong totalPrice = new AtomicLong(0L);
            for (FoodOrderedEntity entity : entities) {
                if (!entity.getBill().getStatus().equals("Chờ xử lý")) {
                    return ResponseEntity.badRequest().body("Không được thêm món ăn vào hóa đơn có trạng thái đã thanh toán hoặc đã hủy");
                }
                totalPrice.getAndSet(totalPrice.get() + entity.getTotalPrice());
                repository.save(entity);
            }
            BillEntity existsEntity = billRepository.findOneById(request.getBillId());
            existsEntity.setTotalPrice(existsEntity.getTotalPrice() + totalPrice.get());

            billRepository.save(existsEntity);

            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm món ăn vào đơn hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (FoodOrderedRequest request) {
        try {
            for (FoodOrderedDetailRequest foodRequest : request.getDetailRequests()) {
                if (!repository.existsById(new FoodOrderedId(request.getBillId(), foodRequest.getFoodId()))) {
                    return ResponseEntity.badRequest().body("Món ăn có mã: " + foodRequest.getFoodId() + " không tồn tại trong đơn hàng có mã: " + request.getBillId());
                }
            }
            List<FoodOrderedEntity> entities = FoodOrderedMapper.mapToEntity(request);

            AtomicLong totalPrice = new AtomicLong(0L);
            for (FoodOrderedEntity entity : entities) {
                if (!entity.getBill().getStatus().equals("Chờ xử lý")) {
                    return ResponseEntity.badRequest().body("Không được xóa món ăn khỏi hóa đơn có trạng thái đã thanh toán hoặc đã hủy");
                }
                totalPrice.getAndSet(totalPrice.get() + entity.getTotalPrice());
                repository.deleteById(entity.getId());
            }
            BillEntity existsEntity = billRepository.findOneById(request.getBillId());
            existsEntity.setTotalPrice(existsEntity.getTotalPrice() - totalPrice.get());

            billRepository.save(existsEntity);

            return ResponseEntity.status(HttpStatus.OK).body("Xóa món ăn vào đơn hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
