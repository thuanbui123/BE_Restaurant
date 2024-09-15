package com.example.restaurant.service;

import com.example.restaurant.entity.BlogPostEntity;
import com.example.restaurant.mapper.BlogPostMapper;
import com.example.restaurant.repository.BlogPostRepository;
import com.example.restaurant.request.BlogPostRequest;
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
public class BlogPostService {
    @Autowired
    private BlogPostRepository repository;

    public ResponseEntity<?> findAll (Pageable pageable) {
        return PaginateUtil.paginate(
                repository::findAll,
                pageable,
                BlogPostMapper::mapToResponse);
    }

    public ResponseEntity<?> findBySlug (String slug, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findBySlugContainingIgnoreCase(slug, pageable),
                pageable,
                BlogPostMapper::mapToResponse
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

    public ResponseEntity<?> addData (BlogPostRequest request) {
        try {
            if (repository.existsByCode(request.getCode())) {
                return ResponseEntity.badRequest().body("Bài viết đã tồn tại!");
            }
            BlogPostEntity entity = BlogPostMapper.mapToEntity(request);
            repository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm bài viết thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi thêm bài viết mới: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateData (String code, BlogPostRequest request) {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.badRequest().body("Không tồn bài viết với mã: " + code);
            }
            BlogPostEntity existsEntity = repository.findOneByCode(code);
            existsEntity.setCode(request.getCode());
            existsEntity.setSlug(Slugify.toSlug(request.getTitle()));
            existsEntity.setTitle(request.getTitle());
            existsEntity.setContent(request.getContent());
            repository.save(existsEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cập nhật bài viết thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi cập nhật bài viết: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (String code) {
        try {
            if (!repository.existsByCode(code)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại bài viết với mã: "+code);
            }
            repository.deleteByCode(code);
            return ResponseEntity.ok().body("Xóa bài viết thành công");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Đã có lỗi xảy ra khi xóa bài viết: " + e.getMessage());
        }
    }
}
