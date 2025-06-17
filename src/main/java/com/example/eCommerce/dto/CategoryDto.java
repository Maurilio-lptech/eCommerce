package com.example.eCommerce.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CategoryDto {

    private UUID id;

    @NotBlank
    private String name;

    @Size(min=0 , max=5000, message = "La descrizione può avere un massimo di 50000 caratteri")
    private String description;

}
