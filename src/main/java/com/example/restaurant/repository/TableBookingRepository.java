package com.example.restaurant.repository;

import com.example.restaurant.entity.TableBookingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface TableBookingRepository extends JpaRepository<TableBookingEntity, Integer> {

    @Query(value = "select * from tablebooking where status = :status" +
            " order by bookingTime desc", nativeQuery = true)
    Page<TableBookingEntity> findByStatus(@Param("status") String status, Pageable pageable);

    @Query(value = "select * from tablebooking where customerId = :id " +
            "order by bookingTime desc", nativeQuery = true)
    Page<TableBookingEntity> findByUserId (@Param("id") Integer id, Pageable pageable);

    @Query(value = "select count(*) from tablebooking where bookingTime = :bookingTime", nativeQuery = true)
    Integer countTableBooking (@Param("bookingTime") String bookingTime);

    @Query(value = "select * from tablebooking where customerId = :id and status = :status", nativeQuery = true)
    TableBookingEntity findByCustomerIdAndStatus (@Param("id") Integer customerId, @Param("status") String status);

    @Query(value = "select * from tablebooking where id = :id", nativeQuery = true)
    TableBookingEntity findOneById (@Param("id") Integer id);

    @Query(value = "SELECT * FROM tablebooking WHERE bookingTime < :timeThreshold AND status = :status", nativeQuery = true)
    List<TableBookingEntity> findLateReservations (@Param("timeThreshold") LocalTime timeThreshold, @Param("status") String status);
}
