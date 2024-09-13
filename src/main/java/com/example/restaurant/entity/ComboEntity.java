package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "combo")
@Setter
@Getter
public class ComboEntity extends BaseEntity{
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

    @ManyToMany
    @JoinTable(
            name = "combofood",
            joinColumns = @JoinColumn(name = "comboId"),
            inverseJoinColumns = @JoinColumn(name = "foodId")
    )
    private List<FoodsEntity> foods;

    @ManyToMany
    @JoinTable(
            name = "comboordered",
            joinColumns = @JoinColumn(name = "comboId"),
            inverseJoinColumns = @JoinColumn(name = "billId")
    )
    private List<BillEntity> bills;
}
