package com.example.eCommerce.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {


    private UUID id;

    private Date orderDate;

    @NotBlank(message = "Stato ordine mancante")
    private String state;

    @NotNull(message = "Id cliente mancante")
    private UUID customer_id;

    @NotBlank(message = "Indirizzo spedizione mancante")
    private String shipmentAddress;

    @NotNull
    @Positive(message = "Il totale deve essere positivo")
    private Double total;


    @NotNull
    private List<OrderDetailsDto> orderDetailsList;


}
