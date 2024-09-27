package com.example.restaurant.service;

import com.example.restaurant.entity.BillEntity;
import com.example.restaurant.entity.ComboEntity;
import com.example.restaurant.entity.ComboOrderEntity;
import com.example.restaurant.entity.EmbeddableId.ComboOrderedId;
import com.example.restaurant.mapper.ComboOrderedMapper;
import com.example.restaurant.repository.BillRepository;
import com.example.restaurant.repository.ComboOrderRepository;
import com.example.restaurant.repository.ComboRepository;
import com.example.restaurant.request.ComboOrderedDetailRequest;
import com.example.restaurant.request.ComboOrderedRequest;
import com.example.restaurant.response.ComboOrderedResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ComboOrderedService {
    @Autowired
    private ComboOrderRepository repository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ComboRepository comboRepository;

    private ComboOrderedResponse findByBillId (Integer billId) {
        List<ComboOrderEntity> entities = repository.findByBillId(billId);
        return (entities != null && !entities.isEmpty()) ? ComboOrderedMapper.mapToResponse(entities) : null;
    }

    public ResponseEntity<?> findData (String prefix, Integer billId) {
        if (prefix.equals("search") && billId != null) {
            return ResponseEntity.ok(findByBillId(billId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
        }
    }

    @Transactional
    public ResponseEntity<?> addData (ComboOrderedRequest request) {
        try {
            for (ComboOrderedDetailRequest comboRequest : request.getRequests()) {
                if (repository.existsById(new ComboOrderedId(comboRequest.getComboId(), request.getBillId()))) {
                    return ResponseEntity.badRequest().body("Combo món ăn có mã: " + comboRequest.getComboId() + " đã tồn tại trong bill có mã: " + request.getBillId());
                }
            }
            List<ComboOrderEntity> entities = ComboOrderedMapper.mapToEntity(request);

            AtomicLong totalPrice = new AtomicLong(0L);
            for (ComboOrderEntity entity : entities) {
                if (!entity.getCombo().getStatus().equals("Đang áp dụng")) {
                    return ResponseEntity.badRequest().body("Không được thêm combo món ăn có trạng thái khác đang áp dụng vào bill");
                }
                if (!entity.getBill().getStatus().equals("Chờ xử lý")) {
                    return ResponseEntity.badRequest().body("Không được thêm combo món ăn vào hóa đơn có trạng thái đã thanh toán hoặc đã hủy");
                }
                totalPrice.getAndSet(totalPrice.get() + entity.getTotalPrice());
                ComboEntity existsCombo = comboRepository.findOneById(entity.getCombo().getId());
                if (existsCombo.getSoldCount() <= 0 || existsCombo.getSoldCount() < entity.getQuantity()) {
                    return ResponseEntity.badRequest().body("Không đủ số lượng combo món ăn để bán!");
                }
                existsCombo.setSoldCount(existsCombo.getSoldCount() - entity.getQuantity());
                comboRepository.save(existsCombo);
                repository.save(entity);
            }
            BillEntity existsEntity = billRepository.findOneById(request.getBillId());
            existsEntity.setTotalPrice(existsEntity.getTotalPrice() + totalPrice.get());

            billRepository.save(existsEntity);

            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm combo món ăn vào đơn hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (ComboOrderedRequest request) {
        try {
            for (ComboOrderedDetailRequest comboRequest : request.getRequests()) {
                if (!repository.existsById(new ComboOrderedId(comboRequest.getComboId(), request.getBillId()))) {
                    return ResponseEntity.badRequest().body("Combo món ăn có mã: " + comboRequest.getComboId() + " không tồn tại trong bill có mã: " + request.getBillId());
                }
            }
            List<ComboOrderEntity> entities = ComboOrderedMapper.mapToEntity(request);

            AtomicLong totalPrice = new AtomicLong(0L);
            for (ComboOrderEntity entity : entities) {
                if (!entity.getBill().getStatus().equals("Chờ xử lý")) {
                    return ResponseEntity.badRequest().body("Không được xóa combo món ăn khỏi hóa đơn có trạng thái đã thanh toán hoặc đã hủy");
                }
                totalPrice.getAndSet(totalPrice.get() + entity.getTotalPrice());
                ComboEntity existsCombo = comboRepository.findOneById(entity.getCombo().getId());

                existsCombo.setSoldCount(existsCombo.getSoldCount() + entity.getQuantity());
                comboRepository.save(existsCombo);
                repository.deleteById(entity.getId());
            }
            BillEntity existsEntity = billRepository.findOneById(request.getBillId());
            existsEntity.setTotalPrice(existsEntity.getTotalPrice() - totalPrice.get());

            billRepository.save(existsEntity);

            return ResponseEntity.status(HttpStatus.OK).body("Xóa combo món ăn vào đơn hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
