package com.example.restaurant.repository;

import com.example.restaurant.entity.EmbeddableId.FoodOrderedId;
import com.example.restaurant.entity.FoodOrderedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderedRepository extends JpaRepository<FoodOrderedEntity, Integer> {

    @Query (value = "select * from foodordered where billId = :id", nativeQuery = true)
    List<FoodOrderedEntity> findByBillId(@Param("id") Integer id);

    boolean existsById(FoodOrderedId id);

    void deleteById (FoodOrderedId id);
}
