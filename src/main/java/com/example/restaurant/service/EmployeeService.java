package com.example.restaurant.service;

import com.example.restaurant.entity.EmployeeEntity;
import com.example.restaurant.mapper.EmployeeMapper;
import com.example.restaurant.repository.EmployeeRepository;
import com.example.restaurant.request.EmployeeRequest;
import com.example.restaurant.utils.PaginateUtil;
import com.example.restaurant.utils.Slugify;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private AccountService accountService;

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                EmployeeMapper::mapToResponse
        );
    }

    public ResponseEntity<?> findBySlug (String slug, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findBySlugContainingIgnoreCase(slug, pageable),
                pageable,
                EmployeeMapper::mapToResponse
        );
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page, size);
        if (prefix.equals("find-all") && query == null) {
            return findAll(pageable);
        } else if (prefix.equals("search") && query != null) {
            return findBySlug(query, pageable);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
    }

    public ResponseEntity<?> addData (EmployeeRequest request) {
        try {
            if (repository.existsByCode(request.getCode())) {
                return ResponseEntity.badRequest().body("Mã nhân viên đã tồn tại!");
            }
            EmployeeEntity entity = EmployeeMapper.mapToEntity(request);
            if (entity == null) {
                return ResponseEntity.badRequest().body("Tài khoản nhân viên không tồn tại!");
            }
            repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm nhân viên thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi thêm nhân viên mới: " + e.getMessage());
        }
    }

    public ResponseEntity<?> updateData (String code, EmployeeRequest request) {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.badRequest().body("Không tồn tại nhân viên với mã " + code);
            }
            EmployeeEntity existsEntity = repository.findOneByCode(code);
            existsEntity.setName(request.getName());
            existsEntity.setSlug(Slugify.toSlug(request.getName()));
            existsEntity.setEmail(request.getEmail());
            existsEntity.setPhoneNumber(request.getPhoneNumber());
            existsEntity.setAddress(request.getAddress());
            existsEntity.setAccount(accountService.findOneById(request.getAccountId()));
            repository.save(existsEntity);
            return ResponseEntity.ok().body("Cập nhât thông tin nhân viên thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật thông tin nhân viên: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (String code) {
        try {
            if(!repository.existsByCode(code)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại nhân viên với mã: " + code);
            }
            repository.deleteByCode(code);
            return ResponseEntity.ok().body("Xóa thông tin nhân viên thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi xóa thông tin nhân viên: "+ e.getMessage());
        }
    }
}
