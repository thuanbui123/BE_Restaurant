package com.example.restaurant.service;

import com.example.restaurant.entity.ComboEntity;
import com.example.restaurant.entity.ComboFoodEntity;
import com.example.restaurant.entity.EmbeddableId.ComboFoodId;
import com.example.restaurant.mapper.ComboFoodMapper;
import com.example.restaurant.repository.ComboFoodRepository;
import com.example.restaurant.repository.ComboRepository;
import com.example.restaurant.request.ComboFoodRequest;
import com.example.restaurant.response.ComboFoodResponse;
import com.example.restaurant.response.ComboResponse;
import com.example.restaurant.utils.PaginateUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ComboFoodService {
    @Autowired
    private ComboFoodRepository repository;

    @Autowired
    private ComboRepository comboRepository;

    private ResponseEntity<?> findAll(Pageable pageable) {
        return PaginateUtil.findDistinctWithPaging(
                p -> repository.findAllByOrderByComboDesc(),
                pageable,
                ComboFoodMapper::mapToResponse,
                ComboResponse::getId,
                true
        );
    }

    private ComboFoodResponse findDetail (Integer comboId) {
        List<ComboFoodEntity> entities = repository.findByComboId(comboId);
        if (entities != null && !entities.isEmpty()) {
            return ComboFoodMapper.mapToDetailResponse(entities);
        }
        return null;
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, Integer comboId) {

        if (prefix.equals("find-all") && comboId == null) {
            Pageable pageable = PageRequest.of(page, size);
            return findAll(pageable);
        } else if (prefix.equals("find-detail") && comboId != null) {
            ComboFoodResponse response = findDetail(comboId);
            if (response != null) {
                return ResponseEntity.ok().body(response);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy combo món ăn!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
    }

    @Transactional
    public ResponseEntity<?> addData(ComboFoodRequest request) {
        try {
            List<ComboFoodEntity> entities = ComboFoodMapper.mapToEntity(request);

            ComboEntity exists = comboRepository.findOneById(request.getComboId());
            if (exists.getStatus().equals("Đã hết hạn") || exists.getStatus().equals("Đang áp dụng")) {
                return ResponseEntity.badRequest().body("Không được thay đổi khi combo món ăn đã hết hạn hoặc đang áp dụng!");
            }
            AtomicLong totalPrice = new AtomicLong(0L);
            for (ComboFoodEntity entity : entities) {
                totalPrice.getAndSet(totalPrice.get() + entity.getTotalPrice() * entity.getAmountOfFood());
            }
            exists.setPrice(totalPrice.get());
            comboRepository.save(exists);
            repository.saveAll(entities);
            return ResponseEntity.badRequest().body("Thêm món ăn vào combo thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (Integer comboId, Integer foodId) {
        try {
            ComboFoodId id = new ComboFoodId(comboId, foodId);
            ComboFoodEntity comboFoodEntity = repository.findById(id);
            ComboEntity exists = comboRepository.findOneById(comboId);
            if (comboFoodEntity == null) {
                return ResponseEntity.badRequest().body("Không tồn tại món ăn trong combo món ăn!");
            }
            if (exists.getStatus().equals("Đã hết hạn") || exists.getStatus().equals("Đang áp dụng")) {
                return ResponseEntity.badRequest().body("Không được thay đổi khi combo món ăn đã hết hạn hoặc đang áp dụng!");
            }

            AtomicLong totalPrice = new AtomicLong(exists.getPrice());
            exists.setPrice(totalPrice.get() - comboFoodEntity.getTotalPrice());
            comboRepository.save(exists);
            repository.deleteById(id);
            return ResponseEntity.ok().body("Xóa món ăn ra khỏi combo món ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
