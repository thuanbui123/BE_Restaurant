package com.example.restaurant.repository;

import com.example.restaurant.entity.OrderedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedRepository extends JpaRepository<OrderedEntity, Integer> {
    @Query(value = "select * from ordered where id = :id", nativeQuery = true)
    OrderedEntity findOneById(@Param("id") Integer id);

    @Query(value = "select * from ordered where customerId = :id and status =:status", nativeQuery = true)
    OrderedEntity findByCustomerIdAndStatus (@Param("id") Integer customerId, @Param("status") String status);

    @Query(value = "select * from ordered where id = :id and status =:status", nativeQuery = true)
    OrderedEntity findByIdAndStatus(@Param("id") Integer id, @Param("status") String status);

    @Query(value = "select * from ordered where customerId = :id", nativeQuery = true)
    List<OrderedEntity> findByCustomerId (@Param("id") Integer customerId);
}
