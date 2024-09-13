package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
public class IngredientsEntity extends BaseEntity {
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "type")
    private String type;

    @Column(name = "quantity")
    private Float quantity;

    @Column(name = "unit")
    private String unit;

    @ManyToOne
    @JoinColumn(name = "supplierId")
    @JsonBackReference
    private SuppliersEntity suppliersEntity;

    @ManyToMany
    @JoinTable(
            name = "ingredientcategorylink",
            joinColumns = @JoinColumn(name = "ingredientId"),
            inverseJoinColumns = @JoinColumn(name = "categoryId")
    )
    private List<IngredientCategoryEntity> ingredientCategoryEntities;

    @ManyToMany
    @JoinTable(
            name = "invoicedetail",
            joinColumns = @JoinColumn(name = "ingredientId"),
            inverseJoinColumns = @JoinColumn(name = "importInvoiceId")
    )
    private List<ImportInvoiceEntity> importInvoiceEntities;
}
