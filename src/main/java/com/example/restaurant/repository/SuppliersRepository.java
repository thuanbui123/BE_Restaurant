package com.example.restaurant.repository;

import com.example.restaurant.entity.SuppliersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SuppliersRepository extends JpaRepository<SuppliersEntity, Integer> {
    Page<SuppliersEntity> findBySlugContainingIgnoreCase(String slug, Pageable pageable);

    boolean existsByCode(String code);

    @Query(value = "select * from suppliers where code = :code", nativeQuery = true)
    SuppliersEntity findOneByCode (@Param("code") String code);

    void deleteByCode(String code);
}
