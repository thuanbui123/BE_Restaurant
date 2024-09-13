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
public class ComboFoodId implements Serializable {
    @Column(name = "comboId")
    private Integer comboId;

    @Column(name = "foodId")
    private Integer foodId;
}
