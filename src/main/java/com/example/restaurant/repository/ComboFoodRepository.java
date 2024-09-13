package com.example.restaurant.repository;

import com.example.restaurant.entity.ComboFoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboFoodRepository extends JpaRepository<ComboFoodEntity, Integer> {
}
