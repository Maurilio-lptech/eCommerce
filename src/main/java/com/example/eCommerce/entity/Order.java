package com.example.eCommerce.entity;

import com.example.eCommerce.enums.OrderState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data

@Table(name = "customer_order")
public class Order implements Serializable {

    @Id
    @UuidGenerator
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date")
    private Date orderDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @NotNull(message = "id customer null")
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @NotBlank(message = "l'indirizzo di consegna non può essere vuoto")
    @Column(name = "shipment_address")
    private String shipmentAddress;

    @NotNull
    @Positive(message = "Il totale deve essere positivo")
    private Double total;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetailsList;


    @PrePersist
    @PreUpdate
    private void validateOrder() {
        if (orderDetailsList == null || orderDetailsList.isEmpty()) {
            this.total = 0.0;
        } else {
            this.total = orderDetailsList.stream()
                    .mapToDouble(od -> od.getQuantity() * od.getPriceForUnit())
                    .sum();
        }
    }


}

