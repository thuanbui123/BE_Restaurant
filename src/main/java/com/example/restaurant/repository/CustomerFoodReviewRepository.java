package com.example.restaurant.repository;

import com.example.restaurant.entity.CustomerFoodReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerFoodReviewRepository extends JpaRepository<CustomerFoodReviewEntity, Integer> {
    @Query(value = "select * from customerfoodreview where customerId = :id", nativeQuery = true)
    List<CustomerFoodReviewEntity> findByCustomerId(@Param("id") Integer id);

    @Query(value = "select * from customerfoodreview where foodId = :id", nativeQuery = true)
    List<CustomerFoodReviewEntity> findByFoodId (@Param("id") Integer id);

    @Query(value = "SELECT foodId AS foodId, AVG(quantityStars) AS avgQuantityStars" +
            " FROM customerfoodreview " +
            "GROUP BY foodId " +
            "ORDER BY AVG(quantityStars) " +
            "DESC LIMIT :limit", nativeQuery = true)
    List<Object[]> findTopFoodsByAverageRating(@Param("limit") int limit);

}
