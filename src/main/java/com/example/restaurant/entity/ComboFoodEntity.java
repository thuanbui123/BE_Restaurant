package com.example.restaurant.entity;

import com.example.restaurant.entity.EmbeddableId.ComboFoodId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "combofood")
@Getter
@Setter
public class ComboFoodEntity {
    @EmbeddedId
    private ComboFoodId id;

    @Column(name = "amountOfFood")
    private Integer amountOfFood;

    @ManyToOne
    @MapsId("comboId")
    @JoinColumn(name = "comboId")
    @JsonBackReference
    private ComboEntity combo;

    @ManyToOne
    @MapsId("foodId")
    @JoinColumn(name = "foodId")
    @JsonBackReference
    private FoodsEntity food;
}
