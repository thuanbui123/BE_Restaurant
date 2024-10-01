package com.example.restaurant.service;

import com.example.restaurant.entity.BillEntity;
import com.example.restaurant.entity.CustomersEntity;
import com.example.restaurant.mapper.BillMapper;
import com.example.restaurant.repository.*;
import com.example.restaurant.request.BillRequest;
import com.example.restaurant.request.CancelBillRequest;
import com.example.restaurant.response.BillResponse;
import com.example.restaurant.utils.PaginateUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {
    @Autowired
    private BillRepository repository;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    AccountInfoRepository accountInfoRepository;

    public boolean existsById (Integer id) {
        return repository.existsById(id);
    }

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAllBillsSortedByStatusAndCreatedDate,
                pageable,
                BillMapper::mapToResponse
        );
    }

    public ResponseEntity<?> findByCode (String code, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findByCodeContainingIgnoreCase(code, pageable),
                pageable,
                BillMapper::mapToResponse
        );
    }

    public BillEntity findOneById (Integer id) {
        return repository.findOneById(id);
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query) {
        try {
            if (prefix.equals("find-all") && query == null) {
                Pageable pageable = PageRequest.of(page, size);
                return findAll(pageable);
            } else if (prefix.equals("search") && query != null) {
                Pageable pageable = PageRequest.of(page, size);
                return findByCode(query, pageable);
                } else if (prefix.equals("get-bill-customer-id") && query!= null) {
                CustomersEntity customersEntity = customersRepository.findOneByAccountId(Integer.valueOf(query));
                List<BillResponse> responses = repository.findByCustomerId(customersEntity.getId()).stream()
                        .map(BillMapper::mapToResponse)
                        .toList();
                return ResponseEntity.ok().body(responses);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> addData (BillRequest request) {
        try {
            if (repository.existsByCode(request.getCode())) {
                return ResponseEntity.badRequest().body("Hóa đơn đã tồn tại!");
            }
            BillEntity entity = BillMapper.mapToEntity(request);
            if (entity == null) {
                return ResponseEntity.badRequest().body("Nhân viên hoặc khách hàng tồn tại!");
            }
            repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm  đơn hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi thêm đơn hàng mới: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateData(String code, BillRequest billRequest) {
        try {
            if (repository.existsByCode(code)) {
                BillEntity existsBill = repository.findOneByCode(code);
                BillEntity billEntityRequest = BillMapper.mapToEntity(billRequest);
                // assert là từ khóa kiểm tra điều kiện tại runtime
                assert billEntityRequest != null;
                if (existsBill.getStatus().equals("Chờ xử lý")) {
                    existsBill.setStatus(billEntityRequest.getStatus());
                    existsBill.setNote(billRequest.getNote());
                    repository.save(existsBill);
                    return ResponseEntity.ok().body("Cập nhật đơn hàng thành công.");
                }
                return ResponseEntity.badRequest().body("Đơn hàng \"đã thanh toán\" hoặc \"đã hủy\" không được phép sửa!");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Đơn hàng " + code + " không tồn tại!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật đơn hàng: "+ e.getMessage());
        }
    }
}
