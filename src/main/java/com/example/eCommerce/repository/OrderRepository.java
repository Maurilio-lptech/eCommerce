package com.example.eCommerce.repository;

import com.example.eCommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order,UUID> {
    public List<Order> findAllByState(String state);
}
