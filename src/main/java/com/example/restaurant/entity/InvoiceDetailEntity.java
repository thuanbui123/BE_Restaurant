package com.example.restaurant.entity;

import com.example.restaurant.entity.EmbeddableId.InvoiceDetailId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoicedetail")
@Getter
@Setter
public class InvoiceDetailEntity {
    @EmbeddedId
    private InvoiceDetailId id;

    @Column(name = "quantity")
    private Float quantity;

    @Column(name = "unit")
    private String unit;

    @Column(name = "note")
    private String note;

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
