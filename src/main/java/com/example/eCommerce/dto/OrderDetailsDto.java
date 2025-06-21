package com.example.eCommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderDetailsDto {

    private UUID id;

    @NotNull(message = "Manca l'id del ordine in un Order Details")
    private UUID order_id;

    @NotNull(message = "Manca l'id del prodotto in un Order Details")
    private UUID product_id;

    @NotNull(message = "Manca la quantità del prodotto in un Order Details")
    private int quantity;

    @NotNull(message = "Manca il prezzo per unita in un Order Details")
    private Double priceForUnit;

    private Double subtotal;


}
