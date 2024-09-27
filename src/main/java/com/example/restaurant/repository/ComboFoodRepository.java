package com.example.restaurant.repository;

import com.example.restaurant.entity.ComboFoodEntity;
import com.example.restaurant.entity.EmbeddableId.ComboFoodId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComboFoodRepository extends JpaRepository<ComboFoodEntity, Integer> {

    @Query(value = "select * " +
            "from combofood " +
            "where comboId = :comboId", nativeQuery = true)
    List<ComboFoodEntity> findByComboId (@Param("comboId") Integer comboId);

    boolean existsById(ComboFoodId id);

    ComboFoodEntity findById(ComboFoodId id);

    void deleteById(ComboFoodId id);

    List<ComboFoodEntity> findAllByOrderByComboDesc();
}
