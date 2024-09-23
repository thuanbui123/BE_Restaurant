package com.example.restaurant.service;

import com.example.restaurant.entity.EmployeeEntity;
import com.example.restaurant.entity.ImportInvoiceEntity;
import com.example.restaurant.mapper.ImportInvoiceMapper;
import com.example.restaurant.repository.ImportInvoiceRepository;
import com.example.restaurant.request.ImportInvoiceRequest;
import com.example.restaurant.utils.PaginateUtil;
import com.example.restaurant.utils.TimeConvertUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ImportInvoiceService {
    @Autowired
    private ImportInvoiceRepository repository;

    @Autowired
    private EmployeeService service;

    public boolean existsById (Integer id) {
        return repository.existsById(id);
    }

    public ImportInvoiceEntity findOneById (Integer id) {
        return repository.findOneById(id);
    }

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                ImportInvoiceMapper::mapToResponse
        );
    }

    public ResponseEntity<?> findByCode (String code, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findByCode(code, pageable),
                pageable,
                ImportInvoiceMapper::mapToResponse
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

    public ResponseEntity<?> addData (ImportInvoiceRequest request) {
        try {
            if (repository.existsByCode(request.getCode())) {
                return ResponseEntity.badRequest().body("Mã phiếu nhập nguyên liệu đã tồn tại!");
            }
            ImportInvoiceEntity entity = ImportInvoiceMapper.mapToEntity(request);
            if (entity == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nhân viên không tồn tai");
            repository.save(entity);
            return ResponseEntity.ok().body("Thêm phiếu nhập nguyên liệu thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi thêm phiếu nhập nguyên liệu mới: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateData (String code, ImportInvoiceRequest request) {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mã phiếu nhập nguyên liệu không tồn tại!");
            }
            ImportInvoiceEntity existsEntity = repository.findOneByCode(code);
            existsEntity.setEntryDate(TimeConvertUtil.convertDateToLocalDateTime(request.getEntryDate()));
            EmployeeEntity employeeEntity = service.findOneById(request.getEmployeeId());
            if (employeeEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nhân viên không tồn tại!");
            }
            existsEntity.setEmployeeEntity(employeeEntity);
            repository.save(existsEntity);
            return ResponseEntity.ok().body("Cập nhật phiếu nhập nguyên liệu thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật phiếu nhập nguyên liệu: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (String code)  {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Phiếu nhập nguyên liệu không tồn tại!");
            }
            repository.deleteByCode(code);
            return ResponseEntity.ok().body("Xóa phiếu nhập nguyên liệu thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi xóa phiếu nhập nguyên liệu: " + e.getMessage());
        }
    }
}
