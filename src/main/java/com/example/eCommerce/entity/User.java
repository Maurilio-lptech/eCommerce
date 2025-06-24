package com.example.eCommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Id;


import java.util.List;
import java.util.UUID;


@Entity
@Data
@ToString
public class User {


    //@GeneratedValue(generator = "uuid2")
    //@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Id
    @UuidGenerator
    private UUID id;

    @Column(unique = true)
    @Email(message = "l'email deve essere valida")
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    private String address;
    @Pattern(regexp = "^[0-9]+$", message = "Il telefono deve contenere solo numeri")
    private String phone;
    //Todo: implementare con spring security role

    // join con le altre tabelle
    @ToString.Exclude
    @OneToMany(mappedBy = "seller")
    private List<Product> product;
    @ToString.Exclude
    @OneToMany(mappedBy = "customer")
    private List<Order> orders;


}
