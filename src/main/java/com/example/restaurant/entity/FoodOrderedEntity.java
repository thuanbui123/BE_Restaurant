package com.example.restaurant.entity;

import com.example.restaurant.entity.EmbeddableId.FoodOrderedId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "foodordered")
@Setter
@Getter
public class FoodOrderedEntity {
    @EmbeddedId
    private FoodOrderedId id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "totalPrice")
    private Long totalPrice;

    @ManyToOne
    @MapsId("billId")
    @JoinColumn(name = "billId")
    @JsonBackReference
    private BillEntity bill;

    @ManyToOne
    @MapsId("foodId")
    @JoinColumn(name = "foodId")
    @JsonBackReference
    private FoodsEntity food;
}
