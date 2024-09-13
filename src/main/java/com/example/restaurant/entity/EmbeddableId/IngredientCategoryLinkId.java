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
public class IngredientCategoryLinkId implements Serializable {
    @Column(name = "ingredientId")
    private Integer ingredientId;

    @Column(name = "categoryId")
    private Integer categoryId;
}
