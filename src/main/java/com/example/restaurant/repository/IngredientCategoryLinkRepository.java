package com.example.restaurant.repository;

import com.example.restaurant.entity.IngredientCategoryLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientCategoryLinkRepository extends JpaRepository<IngredientCategoryLinkEntity, Integer> {
}
