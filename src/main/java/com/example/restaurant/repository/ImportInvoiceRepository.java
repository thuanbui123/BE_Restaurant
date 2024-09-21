package com.example.restaurant.repository;

import com.example.restaurant.entity.ImportInvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportInvoiceRepository extends JpaRepository<ImportInvoiceEntity, Integer> {
    @Query(value = "select * from importinvoice where id = :id", nativeQuery = true)
    ImportInvoiceEntity findOneById (@Param("id") Integer id);

    @Query(value = "select * from importinvoice where code = :code", nativeQuery = true)
    ImportInvoiceEntity findOneByCode (@Param("code") String code);

    Page<ImportInvoiceEntity> findByCode (String code, Pageable pageable);

    boolean existsByCode(String code);

    void deleteByCode(String code);
}
