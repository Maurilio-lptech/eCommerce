package com.example.eCommerce.repository;

import com.example.eCommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoryCustom {
   public Page<Product> search(String nome, String category, Double price, Pageable pageable, Boolean deleted);
}
