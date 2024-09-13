package com.example.restaurant.repository;

import com.example.restaurant.entity.FoodCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategoryEntity, Integer> {
    Page<FoodCategoryEntity> findBySlug(String slug, Pageable pageable);

    Optional<FoodCategoryEntity> findByName(String name);

    @Query(value = "select * from foodcategory where id = :id", nativeQuery = true)
    FoodCategoryEntity findOneById(@Param("id") Integer id);
}
