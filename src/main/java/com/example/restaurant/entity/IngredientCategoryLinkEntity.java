package com.example.restaurant.entity;

import com.example.restaurant.entity.EmbeddableId.IngredientCategoryLinkId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ingredientcategorylink")
@Getter
@Setter
public class IngredientCategoryLinkEntity {
    @EmbeddedId
    private IngredientCategoryLinkId id;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredientId")
    @JsonBackReference
    private IngredientsEntity ingredientsEntity;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "categoryId")
    @JsonBackReference
    private IngredientCategoryEntity ingredientCategoryEntity;
}
