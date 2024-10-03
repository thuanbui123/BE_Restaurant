package com.example.restaurant.repository;

import com.example.restaurant.entity.EmbeddableId.FoodOrderedId;
import com.example.restaurant.entity.FoodOrderedEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FoodOrderedRepository extends JpaRepository<FoodOrderedEntity, Integer> {

    @Query (value = "select * from foodordered where orderedId = :id", nativeQuery = true)
    List<FoodOrderedEntity> findByOrderedId(@Param("id") Integer id);

    @Query(value = "SELECT f.name, SUM(fo.quantity) as totalQuantity " +
            "FROM  FoodOrdered fo " +
            "JOIN Foods f ON fo.foodId = f.id " +
            "JOIN Ordered o ON fo.orderedId = o.id " +
            "WHERE o.created_at BETWEEN :startDate AND :endDate " +
            "GROUP BY f.id " +
            "ORDER BY totalQuantity DESC " +
            "Limit :limit", nativeQuery = true)
    List<Object[]> findTopSellingFoods (@Param("startDate")LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("limit") Integer limit);

    boolean existsById(FoodOrderedId id);

    FoodOrderedEntity findById (FoodOrderedId id);

    void deleteById (FoodOrderedId id);
}
