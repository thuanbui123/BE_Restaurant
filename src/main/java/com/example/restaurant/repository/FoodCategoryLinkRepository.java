package com.example.restaurant.repository;

import com.example.restaurant.entity.EmbeddableId.FoodCategoryLinkId;
import com.example.restaurant.entity.FoodCategoryLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodCategoryLinkRepository extends JpaRepository<FoodCategoryLinkEntity, Integer> {
    @Query(value = "select * from foodcategorylink where categoryId = :id", nativeQuery = true)
    List<FoodCategoryLinkEntity> findFoodByCategoryId (@Param("id") Integer id);

    boolean existsById(FoodCategoryLinkId id);

    void deleteById(FoodCategoryLinkId id);
}
