package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
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

    // Cột status để kiểm soát trạng thái (1: active, 0: inactive)
    @Column(name = "status", nullable = false)
    private String status;

    // Cột sold_count để lưu số lượng combo đã bán
    @Column(name = "sold_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer soldCount;

    // Thời gian bắt đầu áp dụng combo
    @Column(name = "valid_from")
    private Timestamp validFrom;

    // Thời gian kết thúc áp dụng combo
    @Column(name = "valid_to")
    private Timestamp validTo;

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
