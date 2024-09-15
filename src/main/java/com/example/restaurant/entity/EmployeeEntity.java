package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "employees")
@Getter
@Setter
public class EmployeeEntity extends BaseEntity{
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @OneToOne
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private AccountInfo account;

    @OneToMany(mappedBy = "employeeEntity", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ImportInvoiceEntity> importInvoiceEntities;

    @OneToMany(mappedBy = "employeeEntity", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<BlogPostEntity> blogPostEntities;
}
