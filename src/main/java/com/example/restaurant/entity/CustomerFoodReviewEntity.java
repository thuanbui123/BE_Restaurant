package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customerfoodreview")
@Getter
@Setter
public class CustomerFoodReviewEntity extends BaseEntity {

    @Column(name = "quantityStars")
    private Integer quantityStars;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "customerId")
    @JsonBackReference
    private CustomersEntity customers;

    @ManyToOne
    @JoinColumn(name = "foodId")
    @JsonBackReference
    private FoodsEntity foods;

    public Integer getFoodId() {
        return getFoods().getId();
    }
}
