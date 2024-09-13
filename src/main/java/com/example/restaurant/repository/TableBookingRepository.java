package com.example.restaurant.repository;

import com.example.restaurant.entity.TableBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableBookingRepository extends JpaRepository<TableBookingEntity, Integer> {
}
