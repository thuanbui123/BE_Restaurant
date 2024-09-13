package com.example.restaurant.repository;

import com.example.restaurant.entity.FoodCategoryLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodCategoryLinkRepository extends JpaRepository<FoodCategoryLinkEntity, Integer> {
}
