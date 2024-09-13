package com.example.restaurant.repository;

import com.example.restaurant.entity.ImportInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportInvoiceRepository extends JpaRepository<ImportInvoiceEntity, Integer> {
}
