package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo extends BaseEntity{
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "slug")
    private String slug;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "img")
    private String img;
}
