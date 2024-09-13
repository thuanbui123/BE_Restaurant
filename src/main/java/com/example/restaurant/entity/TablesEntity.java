package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tables")
@Getter
@Setter
public class TablesEntity extends BaseEntity {
    @Column(name = "location")
    private String location;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "tablesEntity", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TableBookingEntity> tableBooking;

    @ManyToMany(mappedBy = "tables")
    private List<BillEntity> bills;
}
