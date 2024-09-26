package com.example.restaurant.repository;

import com.example.restaurant.entity.TablesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TablesRepository extends JpaRepository<TablesEntity, Integer> {
    Page<TablesEntity> findByCodeContainingIgnoreCase(String code, Pageable pageable);
    boolean existsByCode(String code);

    @Query(value = "select * from tables where id = :id", nativeQuery = true)
    TablesEntity findOneById (@Param("id") Integer id);

    @Query(value = "select * from tables where code = :code", nativeQuery = true)
    TablesEntity findOneByCode(@Param("code") String code);

    void deleteByCode(String code);
}
