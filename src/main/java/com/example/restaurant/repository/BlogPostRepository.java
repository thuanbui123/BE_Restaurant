package com.example.restaurant.repository;

import com.example.restaurant.entity.BlogPostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Integer> {
    Page<BlogPostEntity> findBySlugContainingIgnoreCase(String slug, Pageable pageable);
    boolean existsByCode(String title);
    @Query(value = "select * from blogpost where code = :code", nativeQuery = true)
    BlogPostEntity findOneByCode (@Param("code") String code);

    void deleteByCode(String code);
}
