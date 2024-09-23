package com.example.restaurant.entity;

import com.example.restaurant.entity.EmbeddableId.InvoiceDetailId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "invoicedetail")
@Getter
@Setter
public class InvoiceDetailEntity {
    @EmbeddedId
    private InvoiceDetailId id;

    @Column(name = "code")
    private String code;

    @Column(name = "quantity")
    private Float quantity;

    @Column(name = "unit")
    private String unit;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredientId")
    @JsonBackReference
    private IngredientsEntity ingredientsEntity;

    @ManyToOne
    @MapsId("importInvoiceId")
    @JoinColumn(name = "importInvoiceId")
    @JsonBackReference
    private ImportInvoiceEntity importInvoiceEntity;
}
