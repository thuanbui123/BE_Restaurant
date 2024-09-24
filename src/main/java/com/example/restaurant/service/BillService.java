package com.example.restaurant.service;

import com.example.restaurant.entity.BillEntity;
import com.example.restaurant.entity.CustomersEntity;
import com.example.restaurant.mapper.BillMapper;
import com.example.restaurant.repository.AccountInfoRepository;
import com.example.restaurant.repository.BillRepository;
import com.example.restaurant.repository.CustomersRepository;
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

    public BillEntity findOneById (Integer employeeId) {
        return repository.findOneById(employeeId);
    }

    public ResponseEntity<?> findByStatus (String status, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findOneByStatus(status, pageable),
                pageable,
                BillMapper::mapToResponse
        );
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
            } else if (prefix.equals("current-invoice") && query == null) {
                Pageable pageable = PageRequest.of(page, size);
                return findByStatus("Chờ xử lý", pageable);
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
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm hóa đơn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi thêm hóa đơn mới: " + e.getMessage());
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
                    existsBill.setCustomer(billEntityRequest.getCustomer());
                    existsBill.setStatus(billEntityRequest.getStatus());
                    existsBill.setNote(billRequest.getNote());
                    existsBill.setTotalPrice(billRequest.getTotalPrice());
                    repository.save(existsBill);
                    return ResponseEntity.ok().body("Cập nhật hóa đơn thành công.");
                }
                return ResponseEntity.badRequest().body("Hóa đơn có trạng thái khác \"Chờ xử lý\" không được phép sửa!");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hóa đơn " + code + " không tồn tại!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật hóa đơn: "+ e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (String code) {
        try {
            if (repository.existsByCode(code)) {
                BillEntity billEntity = repository.findOneByCode(code);
                if (!billEntity.getStatus().equals("Chờ xử lý")) {
                    repository.deleteByCode(code);
                    return ResponseEntity.ok().body("Xóa hóa đơn thành công.");
                }
                return ResponseEntity.badRequest().body("Không được xóa hóa đơn khi chưa hoàn thành!");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hóa đơn " + code + " không tồn tại!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi khi xóa hóa đơn: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> cancelBill (String code, CancelBillRequest request) {
        try {
            if (repository.existsByCode(code)) {
                BillEntity existsBill = repository.findOneByCode(code);
                if (existsBill.getStatus().equals("Chờ xử lý")) {
                    existsBill.setStatus("Đã hủy");
                    existsBill.setNote(request.getNote());
                    repository.save(existsBill);
                    return ResponseEntity.ok().body("Hóa đơn " + code + " hủy thành công.");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không được hủy hóa đơn khi đã hoàn thành!");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hóa đơn " + code + " không tồn tại!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi khi hủy hóa đơn: " + e.getMessage());
        }
    }
}
