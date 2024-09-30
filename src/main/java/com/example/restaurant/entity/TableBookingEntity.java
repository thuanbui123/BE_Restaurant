package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tablebooking")
@Setter
@Getter
public class TableBookingEntity extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "customerId")
    @JsonBackReference
    private CustomersEntity customer;

    @ManyToOne
    @JoinColumn(name = "tableId")
    @JsonBackReference
    private TablesEntity tablesEntity;

    @Column(name = "bookingTime")
    private String bookingTime;

    @Column(name = "status")
    private String status;

    @Column(name = "note")
    private String note;
}
