package com.example.restaurant.service;

import com.example.restaurant.entity.CustomersEntity;
import com.example.restaurant.mapper.CustomerMapper;
import com.example.restaurant.repository.CustomersRepository;
import com.example.restaurant.request.CustomerRequest;
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
public class CustomerService {
    @Autowired
    private CustomersRepository repository;

    @Autowired
    private AccountService accountService;

    public CustomersEntity findOneById (Integer id) {
        return repository.findOneById(id);
    }

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                CustomerMapper::mapToResponse);
    }

    public ResponseEntity<?> findBySlug (String slug, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findBySlugContainingIgnoreCase(slug, pageable),
                pageable,
                CustomerMapper::mapToResponse
        );
    }

    public ResponseEntity<?> findOneByCode (String code) {
        CustomersEntity entity = repository.findOneByCode(code);
        if (entity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại khách hàng " + code);
        }
        return ResponseEntity.ok().body(CustomerMapper.mapToResponse(repository.findOneByCode(code)));
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query) {
        if (prefix.equals("find-all") && query == null) {
            Pageable pageable = PageRequest.of(page, size);
            return findAll(pageable);
        } else if (prefix.equals("search") && query != null) {
            Pageable pageable = PageRequest.of(page, size);
            return findBySlug(query, pageable);
        } else if (prefix.equals("find-one-by-code") && query != null) {
            return findOneByCode(query);
        } else if (prefix.equals("find-one-by-account-id") && query != null) {
            Integer id = Integer.parseInt(query);
            return ResponseEntity.ok().body(repository.findOneByAccountId(id));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
    }

    public ResponseEntity<?> addData (CustomerRequest request) {
        try {
            if (repository.existsByCode(request.getCode())) {
                return ResponseEntity.badRequest().body("Mã khách hàng đã tồn tại!");
            }
            CustomersEntity entity = CustomerMapper.mapToEntity(request);
            if (entity == null) {
                return ResponseEntity.badRequest().body("Tài khoản khách hàng không tồn tại!");
            }
            repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm khách hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi thêm khách hàng mới: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateData (String code, CustomerRequest request) {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.badRequest().body("Không tồn khách hàng với mã: " + code);
            }
            CustomersEntity existsEntity = repository.findOneByCode(code);
            existsEntity.setName(request.getName());
            existsEntity.setSlug(Slugify.toSlug(request.getName()));
            existsEntity.setEmail(request.getEmail());
            existsEntity.setPhoneNumber(request.getPhoneNumber());
            existsEntity.setAddress(request.getAddress());
            existsEntity.setAccount(accountService.findOneById(request.getAccountId()));
            repository.save(existsEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cập nhật thông tin khách hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật thông tin khách hàng: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (String code) {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại khách hàng với mã: "+code);
            }
            repository.deleteByCode(code);
            return ResponseEntity.ok().body("Xóa thông tin khách hàng thành công");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi xóa thông tin khách hàng: " + e.getMessage());
        }
    }
}
