package com.example.eCommerce.repository;

import com.example.eCommerce.entity.OrderDetails;
import com.example.eCommerce.enums.OrderState;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, UUID> {

    public List<OrderDetails> findAllByOrderId(UUID orderId);

    public Optional<OrderDetails> findById(@NotNull UUID id);
    //    @Query("SELECT od FROM order_details od " +
    //            "JOIN customer_order o ON od.order_id=o.id " +
    //            "WHERE state='NEL_carrello'")
    //    List<OrderDetails> findAllCartOrderDetails();

    @Query("SELECT od FROM OrderDetails od " +
            "JOIN od.order o " +
            "WHERE o.state = :state")
    List<OrderDetails> findAllByOrderState(@Param("state") OrderState state);


}
