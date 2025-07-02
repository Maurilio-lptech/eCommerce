package com.example.eCommerce.repository;

import com.example.eCommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {


    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.deleted = true WHERE p.id = :id")
    void deleteById(@Param("id") UUID id);


    Page<Product> findByDeletedFalse(Pageable pageable);

    Optional<Product> findByIdAndDeletedFalse(UUID uuid);

    Page<Product> findByNameContainsAndDeletedFalse(String name, Pageable pageable);
}
