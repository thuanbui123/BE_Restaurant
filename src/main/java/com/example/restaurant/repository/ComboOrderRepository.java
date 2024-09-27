package com.example.restaurant.repository;

import com.example.restaurant.entity.ComboOrderEntity;
import com.example.restaurant.entity.EmbeddableId.ComboOrderedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComboOrderRepository extends JpaRepository<ComboOrderEntity, Integer> {

    @Query(value = "select * from comboordered where billId = :id", nativeQuery = true)
    List<ComboOrderEntity> findByBillId(@Param("id") Integer billId);

    void deleteById (ComboOrderedId id);

    boolean existsById(ComboOrderedId id);
}
