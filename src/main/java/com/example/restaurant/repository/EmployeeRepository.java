package com.example.restaurant.repository;

import com.example.restaurant.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    Page<EmployeeEntity> findBySlugContainingIgnoreCase(String slug, Pageable pageable);

    @Query(value = "select * from employees where code = :code", nativeQuery = true)
    EmployeeEntity findOneByCode (@Param("code") String code);

    @Query(value = "select * from employees where id = :id", nativeQuery = true)
    EmployeeEntity findOneById (@Param("id") Integer id);

    boolean existsByCode (String code);

    void deleteByCode(String code);
}
