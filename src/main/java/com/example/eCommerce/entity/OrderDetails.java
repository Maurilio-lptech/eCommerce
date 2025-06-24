package com.example.eCommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "order_details")
@ToString
@Data
public class OrderDetails {

    @Id
    @UuidGenerator
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull(message = "Manca la quantità del prodotto in un Order Details")
    @Positive(message = "la quantita deve essere positiva")
    private int quantity;

    @NotNull(message = "Manca il prezzo per unita in un Order Details")
    @Positive(message = "Il prezzo deve essere positivo")
    private Double priceForUnit;

    private Double subtotal;

   @PrePersist
    @PreUpdate
    private void calculateSubtotal() {
          this.subtotal = quantity * priceForUnit;
    }



}
