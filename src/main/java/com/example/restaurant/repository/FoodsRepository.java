package com.example.restaurant.repository;

import com.example.restaurant.entity.FoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodsRepository extends JpaRepository<FoodsEntity, Integer> {
    @Query(value = "select * from foods where id like %:id%", nativeQuery = true)
    FoodsEntity findOneById (@Param("id") Integer id);

    @Query(value = "select * from foods where code like %:code%", nativeQuery = true)
    FoodsEntity findOneByCode (@Param("code") String code);

    void deleteByCode(String code);

    Page<FoodsEntity> findBySlugContainingIgnoreCase (String slug, Pageable pageable);

    boolean existsByCode(String code);

    boolean existsByName (String name);
}
