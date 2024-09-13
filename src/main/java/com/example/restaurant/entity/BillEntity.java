package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bill")
@Getter
@Setter
public class BillEntity extends BaseEntity{
    @Column(name = "orderedTime")
    private LocalDateTime orderedTime;

    @Column(name = "totalPrice")
    private Long totalPrice;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private String status;

    @Column(name = "paymentMethod")
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "customerId")
    @JsonBackReference
    private CustomersEntity customer;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    @JsonBackReference
    private EmployeeEntity employee;

    @ManyToMany
    @JoinTable(
            name = "billtable",
            joinColumns = @JoinColumn(name = "billId"),
            inverseJoinColumns = @JoinColumn(name = "tableId")
    )
    private List<TablesEntity> tables;

    @ManyToMany(mappedBy = "bills")
    private List<ComboEntity> combos;

    @ManyToMany
    @JoinTable(
            name = "foodordered",
            joinColumns = @JoinColumn(name = "billId"),
            inverseJoinColumns = @JoinColumn(name = "foodId")
    )
    private List<FoodsEntity> foodsEntities;
}
