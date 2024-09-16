package com.example.restaurant.service;

import com.example.restaurant.entity.TablesEntity;
import com.example.restaurant.mapper.TableMapper;
import com.example.restaurant.repository.TablesRepository;
import com.example.restaurant.request.TablesRequest;
import com.example.restaurant.utils.PaginateUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TablesService {
    @Autowired
    private TablesRepository repository;

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                TableMapper::mapToResponse
        );
    }

    public ResponseEntity<?> findByCode (String code, Pageable pageable) {
        return  PaginateUtil.paginate(
                (pg) -> repository.findByCodeContainingIgnoreCase(code, pageable),
                pageable,
                TableMapper::mapToResponse
        );
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query) {
        Pageable pageable = PageRequest.of(page, size);
        if (prefix.equals("find-all") && query == null) {
            return findAll(pageable);
        } else if (prefix.equals("search") && query != null) {
            return findByCode(query, pageable);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
    }

    public ResponseEntity<?> add (TablesRequest request) {
        try {
            if (repository.existsByCode(request.getCode())) {
                return ResponseEntity.badRequest().body("Mã bàn ăn đã tồn tại!");
            }
            TablesEntity entity = TableMapper.mapToEntity(request);
            repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm bàn ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi bàn ăn mới: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateData (String code, TablesRequest request) {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.badRequest().body("Không tồn tại bàn ăn với mã: " + code);
            }
            TablesEntity existsEntity = repository.findOneByCode(code);
            existsEntity.setLocation(request.getLocation());
            existsEntity.setStatus(request.getStatus());
            repository.save(existsEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cập nhật thông tin bàn ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật thông tin bàn ăn: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (String code) {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại bàn ăn với mã: "+code);
            }
            repository.deleteByCode(code);
            return ResponseEntity.ok().body("Xóa thông tin bàn ăn thành công");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi xóa thông tin bàn ăn: " + e.getMessage());
        }
    }
}
