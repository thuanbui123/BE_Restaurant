package com.example.restaurant.repository;

import com.example.restaurant.entity.ComboEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboRepository extends JpaRepository<ComboEntity, Integer> {
    Page<ComboEntity> findBySlugContainingIgnoreCase(String slug, Pageable pageable);

    boolean existsByCode(String code);

    ComboEntity findOneByCode(String code);

    void deleteByCode(String code);
}
