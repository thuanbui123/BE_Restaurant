package com.example.restaurant.repository;

import com.example.restaurant.entity.BillTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillTableRepository extends JpaRepository<BillTableEntity, Integer> {
}
