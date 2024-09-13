package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "foods")
@Setter
@Getter
public class FoodsEntity extends BaseEntity{
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "img")
    private String img;

    @Column(name = "price")
    private Long price;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "foods")
    private List<ComboEntity> combos;

    @ManyToMany(mappedBy = "foodsEntities")
    private List<BillEntity> billEntities;

    @ManyToMany
    @JoinTable(
            name = "foodcategorylink",
            joinColumns = @JoinColumn(name = "foodId"),
            inverseJoinColumns = @JoinColumn(name = "categoryId")
    )
    private List<FoodCategoryEntity> foodCategoryEntities;
}
