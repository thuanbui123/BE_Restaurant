package com.example.restaurant.repository;

import com.example.restaurant.entity.BillOrderedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillOrderRepository extends JpaRepository<BillOrderedEntity, Integer> {
}
