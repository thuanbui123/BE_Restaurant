package com.example.restaurant.service;

import com.example.restaurant.entity.SuppliersEntity;
import com.example.restaurant.mapper.SupplierMapper;
import com.example.restaurant.repository.SuppliersRepository;
import com.example.restaurant.request.SupplierRequest;
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
public class SupplierService {
    @Autowired
    private SuppliersRepository repository;

    public SuppliersEntity findOneById (Integer id) {
        return repository.findOneById(id);
    }

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                SupplierMapper::mapToResponse);
    }

    public ResponseEntity<?> findBySlug (String slug, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findBySlugContainingIgnoreCase(slug, pageable),
                pageable,
                SupplierMapper::mapToResponse
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

    public ResponseEntity<?> add (SupplierRequest request) {
        try {
            if (repository.existsByCode(request.getCode())) {
                return ResponseEntity.badRequest().body("Mã nhà cung cấp đã tồn tại!");
            }
            SuppliersEntity entity = SupplierMapper.mapToEntity(request);
            repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm nhà cung cấp thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi thêm nhà cung cấp mới: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateData (String code, SupplierRequest request) {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.badRequest().body("Không tồn tại nhà cung cấp với mã: " + code);
            }
            SuppliersEntity existsEntity = repository.findOneByCode(code);
            existsEntity.setName(request.getName());
            existsEntity.setSlug(Slugify.toSlug(request.getName()));
            existsEntity.setEmail(request.getEmail());
            existsEntity.setPhoneNumber(request.getPhoneNumber());
            existsEntity.setAddress(request.getAddress());
            existsEntity.setDescription(request.getDescription());
            repository.save(existsEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cập nhật thông tin nhà cung cấp thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật thông tin nhà cung cấp: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (String code) {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại nhà cung cấp với mã: "+code);
            }
            repository.deleteByCode(code);
            return ResponseEntity.ok().body("Xóa thông tin nhà cung cấp thành công");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi xóa thông tin nhà cung cấp: " + e.getMessage());
        }
    }
}
