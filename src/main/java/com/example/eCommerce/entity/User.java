package com.example.eCommerce.entity;

import com.example.eCommerce.securitySpring.models.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.*;
import java.util.stream.Collectors;


@Entity
@Data
public class User  implements UserDetails {


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

    @OneToMany(mappedBy = "seller")
    private List<Product> product;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String email, String encode) {
    }

    public User() {
        
    }


    // Implementazione UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email; // Usiamo l'email come username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Modifica se hai bisogno di logica per account scaduti
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modifica se hai bisogno di logica per account bloccati
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modifica se hai bisogno di logica per credenziali scadute
    }

    @Override
    public boolean isEnabled() {
        return true; // Modifica se hai bisogno di logica per account disabilitati
    }


}
