package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "bill")
@Getter
@Setter
public class BillEntity extends BaseEntity{
    @Column(name = "code")
    private String code;

    @Column(name = "totalPrice")
    private Long totalPrice;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    @JsonBackReference
    private EmployeeEntity employee;
}
