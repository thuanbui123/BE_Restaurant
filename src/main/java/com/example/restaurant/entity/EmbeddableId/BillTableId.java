package com.example.restaurant.entity.EmbeddableId;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BillTableId implements Serializable {
    @Column(name = "billId")
    private Integer billId;

    @Column(name = "tableId")
    private Integer tableId;
}
