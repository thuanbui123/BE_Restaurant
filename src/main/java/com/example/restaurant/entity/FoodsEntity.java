package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

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
    private List<OrderedEntity> orderedEntities;

    @ManyToMany
    @JoinTable(
            name = "foodcategorylink",
            joinColumns = @JoinColumn(name = "foodId"),
            inverseJoinColumns = @JoinColumn(name = "categoryId")
    )
    private List<FoodCategoryEntity> foodCategoryEntities;

    @OneToMany(mappedBy = "foods", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<CustomerFoodReviewEntity> reviewEntities;
}
