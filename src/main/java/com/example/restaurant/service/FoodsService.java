package com.example.restaurant.service;

import com.example.restaurant.entity.AccountInfo;
import com.example.restaurant.entity.CustomersEntity;
import com.example.restaurant.entity.FoodsEntity;
import com.example.restaurant.mapper.FoodsMapper;
import com.example.restaurant.repository.FoodsRepository;
import com.example.restaurant.request.FoodsRequest;
import com.example.restaurant.utils.PaginateUtil;
import com.example.restaurant.utils.Slugify;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class FoodsService {
    @Autowired
    private FoodsRepository repository;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    public FoodsEntity findOneById (Integer id) {
        return repository.findOneById(id);
    }

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                FoodsMapper::mapToResponse
        );
    }

    public ResponseEntity<?> findBySlug (String slug, Pageable pageable) {
        return  PaginateUtil.paginate(
                (pg) -> repository.findBySlugContainingIgnoreCase(slug, pageable),
                pageable,
                FoodsMapper::mapToResponse
        );
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query, Integer id, Integer limitFood) {

        if (prefix.equals("find-all") && query == null) {
            Pageable pageable = PageRequest.of(page, size);
            return findAll(pageable);
        } else if (prefix.equals("get-food-for-user") && query == null) {
            AccountInfo info = accountService.findOneById(id);
            CustomersEntity customersEntity = customerService.findOneById(info.getId());
            return ResponseEntity.ok().body(recommendationService.recommendFoodsForCustomer(customersEntity.getId(), limitFood));
        } else if (prefix.equals("search") && query != null) {
            Pageable pageable = PageRequest.of(page, size);
            String slug = Slugify.toSlug(query);
            return findBySlug(slug, pageable);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
    }

    public ResponseEntity<?> addFood (FoodsRequest request) {
        try {
            if (repository.existsByCode(request.getCode())) {
                return ResponseEntity.badRequest().body("Mã món ăn đã tồn tại!");
            }
            if (repository.existsByName(request.getName())) {
                return ResponseEntity.badRequest().body("Tên món ăn đã tồn tại!");
            }
            FoodsEntity entity = FoodsMapper.mapToEntity(request);
            repository.save(entity);
            return ResponseEntity.ok().body("Thêm món ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi thêm món ăn mới: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateFood (String code, FoodsRequest request) {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Món ăn không tồn tại!");
            }
            if (repository.existsByName(request.getName())) {
                return ResponseEntity.badRequest().body("Tên món ăn đã tồn tại!");
            }
            FoodsEntity existsEntity = repository.findOneByCode(code);
            existsEntity.setName(request.getName());
            existsEntity.setSlug(Slugify.toSlug(request.getName()));
            existsEntity.setDescription(request.getDescription());
            existsEntity.setImg(request.getImg());
            existsEntity.setPrice(request.getPrice());
            repository.save(existsEntity);
            return ResponseEntity.ok().body("Cập nhật món ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật món ăn: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteFood (String code)  {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Món ăn không tồn tại!");
            }
            repository.deleteByCode(code);
            return ResponseEntity.ok().body("Xóa món ăn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi xóa món ăn: " + e.getMessage());
        }
    }
}
