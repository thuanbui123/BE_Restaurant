package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "ordered")
@Setter
@Getter
public class OrderedEntity extends BaseEntity{

    @Column(name= "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "customerId")
    @JsonBackReference
    private CustomersEntity customer;

    @ManyToMany
    @JoinTable(
            name = "ordertable",
            joinColumns = @JoinColumn(name = "orderedId"),
            inverseJoinColumns = @JoinColumn(name = "tableId")
    )
    private List<TablesEntity> tables;



    @ManyToMany(mappedBy = "ordered")
    private List<ComboEntity> combos;

    @ManyToMany
    @JoinTable(
            name = "foodordered",
            joinColumns = @JoinColumn(name = "orderedId"),
            inverseJoinColumns = @JoinColumn(name = "foodId")
    )
    private List<FoodsEntity> foodsEntities;
}
