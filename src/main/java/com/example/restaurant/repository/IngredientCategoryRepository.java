package com.example.restaurant.repository;

import com.example.restaurant.entity.IngredientCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategoryEntity, Integer> {
}
