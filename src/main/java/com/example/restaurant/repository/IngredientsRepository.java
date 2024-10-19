package com.example.restaurant.repository;

import com.example.restaurant.entity.IngredientsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientsRepository extends JpaRepository<IngredientsEntity, Integer> {
    List<IngredientsEntity> findBySlugContainingIgnoreCase(String slug);

    boolean existsByCode(String code);

    @Query(value = "select * from ingredients where code = :code", nativeQuery = true)
    IngredientsEntity findOneByCode (@Param("code")String code);

    @Query(value = "select * from ingredients where id = :id", nativeQuery = true)
    IngredientsEntity findOneById (@Param("id")Integer id);

    void deleteByCode(String code);
}
