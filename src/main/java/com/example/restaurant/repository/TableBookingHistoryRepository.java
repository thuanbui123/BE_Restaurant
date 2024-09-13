package com.example.restaurant.repository;

import com.example.restaurant.entity.TableBookingHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableBookingHistoryRepository extends JpaRepository<TableBookingHistoryEntity, Integer> {
}
