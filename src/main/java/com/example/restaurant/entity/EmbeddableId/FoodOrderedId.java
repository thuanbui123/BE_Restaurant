package com.example.restaurant.entity.EmbeddableId;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class FoodOrderedId implements Serializable {
    @Column(name = "billId")
    private Integer billId;

    @Column(name = "foodId")
    private Integer foodId;
}
