package com.example.restaurant.entity;

import com.example.restaurant.entity.EmbeddableId.OrderedTableId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ordertable")
@Setter
@Getter
public class OrderedTableEntity {

    @EmbeddedId
    private OrderedTableId id;

    @ManyToOne
    @MapsId("orderedId")
    @JoinColumn(name = "orderedId")
    @JsonBackReference
    private OrderedEntity ordered;

    @ManyToOne
    @MapsId("tableId")
    @JoinColumn(name = "tableId")
    @JsonBackReference
    private TablesEntity table;
}
