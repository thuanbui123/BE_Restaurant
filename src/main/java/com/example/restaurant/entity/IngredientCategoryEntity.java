package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "ingredientscategory")
@Getter
@Setter
public class IngredientCategoryEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "ingredientCategoryEntities")
    private List<IngredientsEntity> ingredientsEntities;
}
