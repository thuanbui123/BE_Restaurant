package com.example.restaurant.entity.EmbeddableId;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class FoodOrderedId implements Serializable {
    @Column(name = "orderedId")
    private Integer orderedId;

    @Column(name = "foodId")
    private Integer foodId;
}
