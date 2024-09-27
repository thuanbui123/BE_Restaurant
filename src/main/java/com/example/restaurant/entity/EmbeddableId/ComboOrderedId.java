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
public class ComboOrderedId implements Serializable {
    @Column(name = "comboId")
    private Integer comboId;

    @Column(name = "billId")
    private Integer billId;
}
