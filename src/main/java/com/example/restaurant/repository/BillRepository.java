package com.example.restaurant.repository;

import com.example.restaurant.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Integer> {
    @Query(value = "select * from bill where customerId = :id", nativeQuery = true)
    List<BillEntity> findByCustomerId (@Param("id") Integer id);
}
