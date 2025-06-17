package com.example.eCommerce.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    private UUID id;

    @NotBlank(message = "Inserire un email")
    @Email(message = "Inserisci un email valida")
    private String email;

    @NotBlank(message = "Inserire una password")
    @Size(min = 6, max = 30, message = "La password deve essere compresa tra 6 e 30 caratteri")
    private String password;
    @NotBlank(message = "Inserire il nome")
    private String name;
    @NotBlank(message = "Inserire il Cognome")
    private String surname;
    private String address;
    @Pattern(regexp = "^[0-9]+$", message = "Il telefono deve contenere solo numeri")
    private String phone;


}
