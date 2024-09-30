package com.example.restaurant.repository;

import com.example.restaurant.entity.OrderedTableEntity;
import com.example.restaurant.entity.EmbeddableId.OrderedTableId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedTableRepository extends JpaRepository<OrderedTableEntity, Integer> {

    @Query(value = "select * from ordertable where tableId = :id", nativeQuery = true)
    List<OrderedTableEntity> findOrderByTableId (@Param("id") Integer id);

    @Query(value = "select * from ordertable where tableId = :tableId and orderedId = :orderedId", nativeQuery = true)
    OrderedTableEntity findOneByTableIdAndOrderedId (@Param("tableId") Integer tableId, @Param("orderedId") Integer orderedId);

    @Query(value = "select * from ordertable where orderedId = :orderedId", nativeQuery = true)
    OrderedTableEntity findOneByOrderedId (@Param("orderedId") Integer orderedId);

    boolean existsById (OrderedTableId id);

    void deleteById (OrderedTableId id);
}
