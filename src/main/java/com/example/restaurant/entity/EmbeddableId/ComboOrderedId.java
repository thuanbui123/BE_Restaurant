package com.example.restaurant.entity.EmbeddableId;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class ComboOrderedId implements Serializable {
    @Column(name = "comboId")
    private Integer comboId;

    @Column(name = "billId")
    private Integer billId;
}
