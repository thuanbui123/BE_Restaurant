package com.example.restaurant.entity;

import com.example.restaurant.entity.EmbeddableId.ComboOrderedId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comboordered")
@Setter
@Getter
public class ComboOrderEntity {
    @EmbeddedId
    private ComboOrderedId id;

    @ManyToOne
    @MapsId("comboId")
    @JoinColumn(name = "comboId")
    @JsonBackReference
    private ComboEntity combo;

    @ManyToOne
    @MapsId("orderedId")
    @JoinColumn(name = "orderedId")
    @JsonBackReference
    private OrderedEntity ordered;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "totalPrice")
    private Long totalPrice;
}
