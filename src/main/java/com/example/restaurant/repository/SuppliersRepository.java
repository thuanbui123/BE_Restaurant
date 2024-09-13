package com.example.restaurant.repository;

import com.example.restaurant.entity.SuppliersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuppliersRepository extends JpaRepository<SuppliersEntity, Integer> {
}
