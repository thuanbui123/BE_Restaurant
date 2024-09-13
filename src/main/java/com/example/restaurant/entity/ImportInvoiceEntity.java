package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "importinvoice")
@Getter
@Setter
public class ImportInvoiceEntity extends BaseEntity{
    @Column(name = "code")
    private String code;

    @Column(name = "entryDate")
    private LocalDateTime entryDate;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    @JsonBackReference
    private EmployeeEntity employeeEntity;

    @ManyToMany(mappedBy = "importInvoiceEntities")
    private List<IngredientsEntity> ingredientsEntities;
}
