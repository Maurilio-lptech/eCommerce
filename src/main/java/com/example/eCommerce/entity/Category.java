package com.example.eCommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity
public class Category {

        @Id
        @UuidGenerator
        private UUID id;

        @Column(unique = true)
        private String name;

        @Lob
        @Column(columnDefinition = "TEXT")
        private String description;


}
