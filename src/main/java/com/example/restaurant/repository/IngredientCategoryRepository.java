package com.example.restaurant.repository;

import com.example.restaurant.entity.IngredientCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategoryEntity, Integer> {
    Page<IngredientCategoryEntity> findBySlugContainingIgnoreCase(String slug, Pageable pageable);

    boolean existsBySlug(String slug);

    @Query(value = "select * from ingredientscategory where id = :id", nativeQuery = true)
    IngredientCategoryEntity findOneById (@Param("id") Integer id);
}
