package com.example.restaurant.repository;

import com.example.restaurant.entity.ComboOrderEntity;
import com.example.restaurant.entity.EmbeddableId.ComboOrderedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComboOrderRepository extends JpaRepository<ComboOrderEntity, Integer> {

    @Query(value = "select * from comboordered where orderedId = :id", nativeQuery = true)
    List<ComboOrderEntity> findByOrderedId(@Param("id") Integer orderedId);

    @Query(value = "SELECT c.name, SUM(co.quantity) as totalQuantity " +
            "FROM  ComboOrdered co " +
            "JOIN Combo c ON co.comboId = c.id " +
            "JOIN Ordered o ON co.orderedId = o.id " +
            "WHERE o.created_at BETWEEN :startDate AND :endDate " +
            "GROUP BY c.id " +
            "ORDER BY totalQuantity DESC " +
            "Limit :limit", nativeQuery = true)
    List<Object[]> findTopSellingCombos (@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("limit") Integer limit);

    ComboOrderEntity findOneById (ComboOrderedId id);

    void deleteById (ComboOrderedId id);

    boolean existsById(ComboOrderedId id);
}
