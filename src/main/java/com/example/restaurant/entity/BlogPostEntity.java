package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "blogpost")
@Getter
@Setter
public class BlogPostEntity extends BaseEntity{

    @Column(name = "code")
    private String code;

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    @JsonBackReference
    private EmployeeEntity employeeEntity;
}
