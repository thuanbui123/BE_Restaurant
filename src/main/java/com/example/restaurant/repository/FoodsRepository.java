package com.example.restaurant.repository;

import com.example.restaurant.entity.FoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodsRepository extends JpaRepository<FoodsEntity, Integer> {
    @Query(value = "select * from foods where name like %:id%", nativeQuery = true)
    FoodsEntity findOneById (@Param("id") Integer id);
}
