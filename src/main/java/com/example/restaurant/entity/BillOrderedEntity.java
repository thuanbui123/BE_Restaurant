package com.example.restaurant.entity;

import com.example.restaurant.entity.EmbeddableId.BillOrderedId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "billordered")
@Setter
@Getter
public class BillOrderedEntity {
    @EmbeddedId
    private BillOrderedId id;

    @ManyToOne
    @MapsId("billId")
    @JoinColumn(name = "billId")
    @JsonBackReference
    private BillEntity bill;

    @ManyToOne
    @MapsId("orderedId")
    @JoinColumn(name = "orderedId")
    @JsonBackReference
    private OrderedEntity ordered;
}
