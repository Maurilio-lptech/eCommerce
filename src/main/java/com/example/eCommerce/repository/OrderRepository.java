package com.example.eCommerce.repository;

import com.example.eCommerce.entity.Order;
import com.example.eCommerce.enums.OrderState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order,UUID> {
    public List<Order> findAllByState(OrderState state);
    public List<Order> findAllByStateAndCustomerId(OrderState state, UUID customerID);
    public Optional<Order> findByStateAndCustomerId(OrderState state, UUID customerID);
    public Optional<Order> findByIdAndCustomerId(UUID id,  UUID customerID);
    public Page<Order> findAllByStateAndCustomerId(OrderState state, UUID customerID, Pageable pageable);
    Page<Order> findAllByCustomerId(UUID customerId, Pageable pageable);
}
