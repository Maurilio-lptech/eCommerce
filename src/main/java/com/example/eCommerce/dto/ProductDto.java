package com.example.eCommerce.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


import java.util.UUID;

@Data
public class ProductDto {

    private UUID id;

    @NotBlank
    @Size(min = 3, max = 100, message = "Inserire un massimo di 100 caratteri e un minimo di 3")
    private String name;

    @Size(min = 0, max = 5000, message = "Inserire un massimo di 5000 caratteri")
    private String description;

    @NotNull
    @DecimalMin(value = "00.01")
    private double price;

    @NotNull
    @Min(value = 0, message = "la quantita disponibile non può essere minore di 0")
    private int quantity_available;

    @NotNull
    private UUID seller_id;
    private String sellerName;

    @NotNull
    private UUID category_id;
    private String categoryName;










}
