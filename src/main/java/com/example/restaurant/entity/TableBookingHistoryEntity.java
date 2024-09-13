package com.example.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tablebookinghistory")
@Getter
@Setter
public class TableBookingHistoryEntity extends BaseEntity {
    @Column(name = "oldStatus")
    private String oldStatus;

    @Column(name = "newStatus")
    private String newStatus;

    @Column(name = "changeTime")
    private LocalDateTime changeTime;

    @Column(name = "changeBy")
    private String changeBy;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "bookingTableId")
    @JsonBackReference
    private TableBookingEntity tableBooking;
}
