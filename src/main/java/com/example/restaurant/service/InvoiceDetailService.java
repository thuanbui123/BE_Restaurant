package com.example.restaurant.service;

import com.example.restaurant.entity.EmbeddableId.InvoiceDetailId;
import com.example.restaurant.entity.ImportInvoiceEntity;
import com.example.restaurant.entity.IngredientsEntity;
import com.example.restaurant.entity.InvoiceDetailEntity;
import com.example.restaurant.mapper.InvoiceDetailMapper;
import com.example.restaurant.repository.IngredientsRepository;
import com.example.restaurant.repository.InvoiceDetailRepository;
import com.example.restaurant.request.IngredientDetailRequest;
import com.example.restaurant.request.InvoiceDetailRequest;
import com.example.restaurant.response.InvoiceDetailResponse;
import com.example.restaurant.response.InvoiceResponse;
import com.example.restaurant.utils.PaginateUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class InvoiceDetailService {
    @Autowired
    private InvoiceDetailRepository repository;

    @Autowired
    private IngredientsRepository ingredientsRepository;

    private ResponseEntity<?> findAll(Pageable pageable) {
        return PaginateUtil.findDistinctWithPaging(
                p -> repository.findAllByOrderByCreatedAtDesc(),
                pageable,
                InvoiceDetailMapper::mapToResponse,
                InvoiceResponse::getEntryDate,
                true
        );
    }

    private InvoiceDetailResponse findDetail (String code, Integer importInvoiceId) {
        List<InvoiceDetailEntity> entities = repository.findByCodeAndImportInvoiceId(code, importInvoiceId);
        if (entities != null && !entities.isEmpty()) {
            return InvoiceDetailMapper.mapToDetailResponse(entities);
        }
        return null;
    }

    private ResponseEntity<?> findByCode (String code, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findByCodeContainingIgnoreCase(code, pageable),
                pageable,
                InvoiceDetailMapper::mapToResponse
        );
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query, Integer importInvoiceId) {

        if (prefix.equals("find-all") && query == null && importInvoiceId == null) {
            Pageable pageable = PageRequest.of(page, size);
            return findAll(pageable);
        } else if (prefix.equals("search") && query != null && importInvoiceId == null) {
            Pageable pageable = PageRequest.of(page, size);
            return findByCode(query, pageable);
        } else if (prefix.equals("find-detail") && query != null && importInvoiceId != null) {
            InvoiceDetailResponse response = findDetail(query, importInvoiceId);
            if (response != null) {
                return ResponseEntity.ok().body(response);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phiếu nhập!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
    }

    public ResponseEntity<?> addData(InvoiceDetailRequest request) {
        try {
            List<InvoiceDetailEntity> entities = InvoiceDetailMapper.mapToEntity(request);
            long count = entities.stream()
                    .filter(entity -> entity.getIngredientsEntity() != null)
                    .filter(entity -> {
                        InvoiceDetailId id = new InvoiceDetailId(entity.getIngredientsEntity().getId(), request.getImportInvoiceId());
                        return repository.existsByCodeAndId(request.getCode(), id);
                    })
                    .count();
            if (count > 0) {
                return ResponseEntity.badRequest().body("Nguyên liệu đã tồn tại trong phiếu nhập");
            }
            repository.saveAll(entities);
            return ResponseEntity.badRequest().body("Thêm nguyên liệu vào phiếu nhập nguyên liệu thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> addIngredientsToInvoice (String code, List<IngredientDetailRequest> request) {
        try {
            if (request== null || request.isEmpty()) {
                return ResponseEntity.badRequest().body("Danh sách nguyên liệu không được rỗng.");
            }
            List<InvoiceDetailEntity> entities = repository.findByCode(code);
            InvoiceDetailId id = new InvoiceDetailId();
            ImportInvoiceEntity importInvoiceEntity = new ImportInvoiceEntity();
            // Duyệt qua danh sách nguyên liệu và kiểm tra xem có nguyên liệu nào đã tồn tại trong phiếu nhập
            if (entities != null && !entities.isEmpty()) {
                for (IngredientDetailRequest ingredientDetail : request) {
                    id = new InvoiceDetailId(ingredientDetail.getIngredientId(), entities.get(0).getImportInvoiceEntity().getId());
                    importInvoiceEntity = entities.get(0).getImportInvoiceEntity();
                    if (repository.existsByCodeAndId(code, id)) {
                        return ResponseEntity.badRequest().body("Nguyên liệu " + ingredientDetail.getIngredientId() + " đã tồn tại trong phiếu nhập.");
                    }
                }
            } else {
                return ResponseEntity.badRequest().body("Phiếu nhập nguyên liệu " + code + " không tồn tại!");
            }
            // Nếu không có nguyên liệu nào tồn tại, thêm nguyên liệu mới vào phiếu nhập
            List<InvoiceDetailEntity> newEntities = new ArrayList<>();
            for (IngredientDetailRequest ingredientDetail : request) {
                InvoiceDetailEntity newEntity = new InvoiceDetailEntity();
                IngredientsEntity ingredientsEntity = ingredientsRepository.findOneById(ingredientDetail.getIngredientId());
                if (ingredientsEntity != null) {
                    newEntity.setId(id);
                    newEntity.setCode(code);
                    newEntity.setImportInvoiceEntity(importInvoiceEntity);
                    newEntity.setIngredientsEntity(ingredientsEntity);
                    newEntity.setQuantity(ingredientDetail.getQuantity());
                    newEntity.setUnit(ingredientDetail.getUnit());
                    newEntity.setNote(ingredientDetail.getNote());
                    newEntity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                    newEntities.add(newEntity);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nguyên liệu có mã: " + ingredientDetail.getIngredientId());
                }
            }
            repository.saveAll(newEntities);
            return ResponseEntity.ok().body("Thêm nguyên liệu vào phiếu nhập thành công.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> removeIngredientFromInvoice (String code, Integer ingredientId) {
        try {
            List<InvoiceDetailEntity> entities = repository.findByCode(code);

            if (entities == null || entities.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Phiếu nhập nguyên liệu " + code + " không tồn tại!");
            }

            InvoiceDetailId id = new InvoiceDetailId(ingredientId, entities.get(0).getImportInvoiceEntity().getId());

            if (repository.existsByCodeAndId(code, id)) {
                repository.deleteByCodeAndId(code, id);
                return ResponseEntity.ok().body("Xóa nguyên liệu khỏi phiếu nhập thành công.");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nguyên liệu " + ingredientId + " không tồn tại trong phiếu nhập.");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateData (String code, InvoiceDetailRequest request) {
        try {
            List<InvoiceDetailEntity> entities = InvoiceDetailMapper.mapToEntity(request);
            List<InvoiceDetailEntity> update = entities.stream()
                    .filter(entity -> entity.getIngredientsEntity() != null)
                    .flatMap(entity -> {
                        InvoiceDetailId id = new InvoiceDetailId(entity.getIngredientsEntity().getId(), entity.getImportInvoiceEntity().getId());
                        Optional<InvoiceDetailEntity> existsInvoiceDetails = repository.findByCodeAndId(code, id);

                        return existsInvoiceDetails.map(existsInvoiceDetail -> {
                            existsInvoiceDetail.setQuantity(entity.getQuantity());
                            existsInvoiceDetail.setUnit(entity.getUnit());
                            existsInvoiceDetail.setNote(entity.getNote());
                            return Stream.of(existsInvoiceDetail);
                        }).orElseGet(Stream::empty);
                    })
                    .toList();
            if (!update.isEmpty()) {
                repository.saveAll(update);
                return ResponseEntity.ok().body("Cập nhật phiếu nhập nguyên liệu thành công");
            }
            return ResponseEntity.badRequest().body("Không tồn tại phiếu nhập với mã: "+code);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (String code) {
        try {
            List<InvoiceDetailEntity> invoiceDetails = repository.findByCode(code);
            if (!invoiceDetails.isEmpty()) {
                // Xóa tất cả thực thể tìm thấy
                repository.deleteAll(invoiceDetails);
                return ResponseEntity.ok().body("Xóa phiếu nhập nguyên liệu thành công.");
            } else {
                return ResponseEntity.badRequest().body("Không tồn tại phiếu nhập với mã: "+ code);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
