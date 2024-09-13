package com.example.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "foodcategory")
@Setter
@Getter
public class FoodCategoryEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "foodCategoryEntities")
    private List<FoodsEntity> foodsEntityList;
}
