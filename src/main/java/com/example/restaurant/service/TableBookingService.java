package com.example.restaurant.service;

import com.example.restaurant.entity.TableBookingEntity;
import com.example.restaurant.mapper.TableBookingMapper;
import com.example.restaurant.repository.CustomersRepository;
import com.example.restaurant.repository.TableBookingRepository;
import com.example.restaurant.repository.TablesRepository;
import com.example.restaurant.request.TableBookingRequest;
import com.example.restaurant.utils.PaginateUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TableBookingService {
    @Autowired
    private TableBookingRepository repository;

    @Autowired
    private TablesRepository tablesRepository;

    @Autowired
    private CustomersRepository customersRepository;

    private ResponseEntity<?> findByStatus (String status, Pageable pageable) {
        return PaginateUtil.paginate(
                pg -> repository.findByStatus(status, pageable),
                pageable,
                TableBookingMapper::mapToResponse
        );
    }

    private ResponseEntity<?> findByCustomerId (Integer id, Pageable pageable) {
        return PaginateUtil.paginate(
                pg -> repository.findByUserId(id, pageable),
                pageable,
                TableBookingMapper::mapToResponse
        );
    }

    private ResponseEntity<?> findOneById (Integer id) {
        TableBookingEntity entity = repository.findOneById(id);
        if (entity == null) {
            return ResponseEntity.badRequest().body("Lịch đặt bàn có mã: " + id + " không tồn tại!");
        } else {
            return ResponseEntity.ok().body(TableBookingMapper.mapToResponse(entity));
        }
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query, Integer id) {
        if (prefix.equals("find-by-status") && query != null && id == null) {
            Pageable pageable = PageRequest.of(page, size);
            return findByStatus(query, pageable);
        } else if (prefix.equals("find-by-customer-id") && query == null && id != null) {
            Pageable pageable = PageRequest.of(page, size);
            return findByCustomerId(id, pageable);
        } else if (prefix.equals("find-by-id") && query == null && id != null) {
            return findOneById(id);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
    }

    public ResponseEntity<?> addData (TableBookingRequest request) {
        try {
            TableBookingEntity exists = repository.findByCustomerIdAndStatus(request.getCustomerId(), "Đã đặt bàn");
            if (exists != null) {
                return ResponseEntity.badRequest().body("Khách hàng đã đặt bàn tại nhà hàng!");
            }
            exists = repository.findByCustomerIdAndStatus(request.getCustomerId(), "Khách nhận bàn");
            if (exists != null) {
                return ResponseEntity.badRequest().body("Khách hàng đang dùng bữa tại nhà hàng!");
            }
            Integer countTableBooking = repository.countTableBooking(request.getIntervalTime());
            Integer totalTable = tablesRepository.totalTable();
            if (countTableBooking == Math.ceil(0.3 * totalTable)) {
                return ResponseEntity.badRequest().body("Bàn đã được đặt hết trong khung giờ: " + request.getIntervalTime() + "!");
            }
            TableBookingEntity entity = TableBookingMapper.mapToEntity(request);
            repository.save(entity);
            return ResponseEntity.ok().body("Đặt bàn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateData (Integer id, TableBookingRequest request) {
        try {
            if (!repository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại phiếu đặt bàn có mã: " + id);
            }
            TableBookingEntity exists = repository.findOneById(id);
            if (!exists.getStatus().equals("Đã đặt bàn")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không được sửa phiếu đặt bàn có trạng thái khác trạng thái đã đặt bàn!");
            }
            Integer countTableBooking = repository.countTableBooking(request.getIntervalTime());
            Integer totalTable = tablesRepository.totalTable();
            if (countTableBooking == Math.ceil(0.3 * totalTable)) {
                return ResponseEntity.badRequest().body("Bàn đã được đặt hết trong khung giờ: " + request.getIntervalTime() + "!");
            }
            exists.setCustomer(customersRepository.findOneById(request.getCustomerId()));
            exists.setIntervalTime(request.getIntervalTime());
            exists.setNote(request.getNote());
            repository.save(exists);
            return ResponseEntity.ok().body("Sửa phiếu đặt bàn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> cancelData (Integer id, TableBookingRequest request) {
        try {
            if (!repository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại phiếu đặt bàn có mã: " + id);
            }
            TableBookingEntity exists = repository.findOneById(id);
            if (!exists.getStatus().equals("Đã đặt bàn")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không được hủy phiếu đặt bàn có trạng thái khác trạng thái đã đặt bàn!");
            }
            exists.setStatus("Đã hủy");
            exists.setNote(request.getNote());
            repository.save(exists);
            return ResponseEntity.ok().body("Hủy đặt bàn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
