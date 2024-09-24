package com.example.restaurant.repository;

import com.example.restaurant.entity.BillEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Integer> {
    @Query(value = "select * from bill b " +
            "where b.customerId = :id " +
            "ORDER BY CASE WHEN b.status = 'chờ xử lý' THEN 0 ELSE 1 END, b.created_at DESC",
            nativeQuery = true)
    List<BillEntity> findByCustomerId (@Param("id") Integer id);

    boolean existsByCode(String code);

    @Query(value = "select * from bill where id = :id", nativeQuery = true)
    BillEntity findOneById (@Param("id") Integer id);

    @Query(value = "select * from bill where code = :code", nativeQuery = true)
    BillEntity findOneByCode (@Param("code") String code);

    @Query(value = "select * from bill where status = :status", nativeQuery = true)
    Page<BillEntity> findOneByStatus (@Param("status") String status, Pageable pageable);

    @Query(value = "SELECT * FROM bill b ORDER BY CASE WHEN b.status = 'chờ xử lý' THEN 0 ELSE 1 END, b.created_at DESC", nativeQuery = true)
    Page<BillEntity> findAllBillsSortedByStatusAndCreatedDate(Pageable pageable);

    Page<BillEntity> findByCodeContainingIgnoreCase(String code, Pageable pageable);

    void deleteByCode (String code);
}
