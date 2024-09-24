package com.example.restaurant.repository;

import com.example.restaurant.entity.CustomersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomersRepository extends JpaRepository<CustomersEntity, Integer> {
    Page<CustomersEntity> findBySlugContainingIgnoreCase(String slug, Pageable pageable);

    boolean existsByCode(String code);

    @Query(value = "select * from customers where code = :code", nativeQuery = true)
    CustomersEntity findOneByCode (@Param("code") String code);

    @Query(value = "select * from customers where id = :id", nativeQuery = true)
    CustomersEntity findOneById (@Param("id") Integer id);

    @Query(value = "select * from customers where accountId = :id", nativeQuery = true)
    CustomersEntity findOneByAccountId (@Param("id") Integer id);

    @Query(value = "select * from customers where name = :name", nativeQuery = true)
    CustomersEntity findOneByName (@Param("name") String name);

    void deleteByCode(String code);
}
