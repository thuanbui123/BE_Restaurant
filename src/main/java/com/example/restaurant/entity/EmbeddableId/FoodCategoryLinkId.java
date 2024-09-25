package com.example.restaurant.entity.EmbeddableId;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FoodCategoryLinkId implements Serializable {
    @Column(name = "foodId")
    private Integer foodId;

    @Column(name = "categoryId")
    private Integer categoryId;
}
