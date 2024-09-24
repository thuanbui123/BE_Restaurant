package com.example.restaurant.repository;

import com.example.restaurant.entity.EmbeddableId.IngredientCategoryLinkId;
import com.example.restaurant.entity.IngredientCategoryLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientCategoryLinkRepository extends JpaRepository<IngredientCategoryLinkEntity, Integer> {

    @Query(value = "select * from ingredientcategorylink where categoryId = :id", nativeQuery = true)
    List<IngredientCategoryLinkEntity> findIngredientsByCategoryId (@Param("id") Integer id);

    boolean existsById(IngredientCategoryLinkId id);

    void deleteById(IngredientCategoryLinkId id);
}
