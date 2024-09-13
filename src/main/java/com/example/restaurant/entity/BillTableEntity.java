package com.example.restaurant.entity;

import com.example.restaurant.entity.EmbeddableId.BillTableId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "billtable")
@Setter
@Getter
public class BillTableEntity {

    @EmbeddedId
    private BillTableId id;

    @ManyToOne
    @MapsId("billId")
    @JoinColumn(name = "billId")
    @JsonBackReference
    private BillEntity bill;

    @ManyToOne
    @MapsId("tableId")
    @JoinColumn(name = "tableId")
    @JsonBackReference
    private TablesEntity table;
}
