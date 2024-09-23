package com.example.restaurant.entity.EmbeddableId;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailId implements Serializable {
    @Column(name = "ingredientId")
    private Integer ingredientId;

    @Column(name = "importInvoiceId")
    private Integer importInvoiceId;
}
