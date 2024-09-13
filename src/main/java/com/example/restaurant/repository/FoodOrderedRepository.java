package com.example.restaurant.repository;

import com.example.restaurant.entity.FoodOrderedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodOrderedRepository extends JpaRepository<FoodOrderedEntity, Integer> {
}
