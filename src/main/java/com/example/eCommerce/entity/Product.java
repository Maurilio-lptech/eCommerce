package com.example.eCommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Product {

    @Id
    @UuidGenerator
    private UUID id;

    @NotBlank
    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Positive(message = "Il prezzo deve essere positivo")
    @DecimalMin(value = "00.01")
    private double price;
    @ColumnDefault("0")
    private int quantity_available;

    @ManyToOne
    @JoinColumn(name="seller_id")
    private User seller;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;


    private String imageName;

    @OneToMany(mappedBy = "product")
    private List<OrderDetails> orderDetailsList;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean deleted;




}
