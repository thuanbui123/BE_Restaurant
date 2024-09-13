package com.example.restaurant.entity;

import com.example.restaurant.entity.EmbeddableId.FoodCategoryLinkId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "foodcategorylink")
@Getter
@Setter
public class FoodCategoryLinkEntity {
    @EmbeddedId
    private FoodCategoryLinkId id;

    @ManyToOne
    @MapsId("foodId")
    @JoinColumn(name = "foodId")
    @JsonBackReference
    private FoodsEntity foods;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "categoryId")
    @JsonBackReference
    private FoodCategoryEntity foodCategory;
}
