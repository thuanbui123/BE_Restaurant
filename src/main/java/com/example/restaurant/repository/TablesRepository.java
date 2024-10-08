package com.example.restaurant.repository;

import com.example.restaurant.entity.TablesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TablesRepository extends JpaRepository<TablesEntity, Integer> {
    Page<TablesEntity> findByCodeContainingIgnoreCase(String code, Pageable pageable);

    @Query(value = "select * from tables where code <> 'TBL000'", nativeQuery = true)
    Page<TablesEntity> findAllTable (Pageable pageable);

    boolean existsByCode(String code);

    @Query(value = "SELECT DISTINCT t.location FROM tables t where id <> 1", nativeQuery = true)
    List<String> findDistinctLocations();

    List<TablesEntity> findByLocation(String location);

    @Query(value = "select * from tables where id <> 1", nativeQuery = true)
    List<TablesEntity> findAll();

    @Query(value = "select count(*) from tables where code <> 'TBL000'", nativeQuery = true)
    Integer totalTable();

    @Query(value = "select * from tables where id = :id", nativeQuery = true)
    TablesEntity findOneById (@Param("id") Integer id);

    @Query(value = "select * from tables where code = :code", nativeQuery = true)
    TablesEntity findOneByCode(@Param("code") String code);

    void deleteByCode(String code);
}
