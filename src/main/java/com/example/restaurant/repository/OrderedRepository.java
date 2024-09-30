package com.example.restaurant.repository;

import com.example.restaurant.entity.OrderedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedRepository extends JpaRepository<OrderedEntity, Integer> {
    @Query(value = "select * from ordered where id = :id", nativeQuery = true)
    OrderedEntity findOneById(@Param("id") Integer id);
}
