package com.example.restaurant.repository;

import com.example.restaurant.entity.BillTableEntity;
import com.example.restaurant.entity.EmbeddableId.BillTableId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillTableRepository extends JpaRepository<BillTableEntity, Integer> {

    @Query(value = "select * from billtable where tableId = :id", nativeQuery = true)
    List<BillTableEntity> findBillByTableId (@Param("id") Integer id);

    @Query(value = "select * from billtable where tableId = :tableId and billId = :billId", nativeQuery = true)
    BillTableEntity findOneByTableIdAndBillId (@Param("tableId") Integer tableId, @Param("billId") Integer billId);

    @Query(value = "select * from billtable where billId = :billId", nativeQuery = true)
    BillTableEntity findOneByBillId (@Param("billId") Integer billId);

    boolean existsById (BillTableId id);

    void deleteById (BillTableId id);
}
