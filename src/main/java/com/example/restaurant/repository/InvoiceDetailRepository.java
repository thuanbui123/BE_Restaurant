package com.example.restaurant.repository;

import com.example.restaurant.entity.EmbeddableId.InvoiceDetailId;
import com.example.restaurant.entity.InvoiceDetailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetailEntity, Integer> {

    Page<InvoiceDetailEntity> findByCodeContainingIgnoreCase(String code, Pageable pageable);

    @Query(value = "select * " +
            "from invoicedetail " +
            "where code = :code and importInvoiceId = :id", nativeQuery = true)
    List<InvoiceDetailEntity> findByCodeAndImportInvoiceId(@Param("code") String code, @Param(("id")) Integer importInvoiceId);

    List<InvoiceDetailEntity> findAllByOrderByCreatedAtDesc();

    Optional<InvoiceDetailEntity> findByCodeAndId(String code, InvoiceDetailId id);

    boolean existsByCodeAndId(String code, InvoiceDetailId id);

    List<InvoiceDetailEntity> findByCode(String code);

    void deleteByCodeAndId (String code, InvoiceDetailId id);

    void deleteByCode(String code);
}
