package com.example.eCommerce.controller;
import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.securitySpring.security.services.UserDetailsImpl;
import com.example.eCommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService service;

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    @PostMapping("admin/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDto> createOrder(@RequestBody @NotNull OrderDto EntityToCreate){
        if(EntityToCreate.getId()!=null){
            EntityToCreate.setId(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(EntityToCreate));
    }

    @PutMapping("admin/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDto> updateOrder(@RequestBody @NotNull OrderDto updatedEntity){
        return ResponseEntity.ok(service.updateOrder(updatedEntity));
    }

    @DeleteMapping("admin/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable @NotNull UUID id){
        service.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id){
        return ResponseEntity.ok(service.getOrderById(id));
    }

    @GetMapping("admin/AllOrder")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OrderDto> findAllOrder(Pageable pageable){
        return service.getAllOrders(pageable);
    }

    //TODO: paginazione da implementare
    @GetMapping("admin/AllOrder/{state}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDto> findAllOrderByState(@PathVariable String state){
        return service.getAllOrdersByState(state);
    }

    //GESTIONE  ORDINI UTENTE
    @GetMapping("/AllOrder")
    @PreAuthorize("hasRole('USER')")
    public Page<OrderDto> findAllUserOrder(Pageable pageable) {
        UUID customer_id = getCurrentUserId();
        return service.getAllUserOrders(customer_id, pageable);
    }

    @GetMapping("/AllOrder/{state}")
    @PreAuthorize("hasRole('USER')")
    public Page<OrderDto> findAllOrderUserByState(@PathVariable String state, Pageable pageable){
        UUID customer_id = getCurrentUserId();
        return service.getAllUserOrdersByState(customer_id,state, pageable);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public OrderDto findUserOrderbyId(@PathVariable UUID orderId) {
        UUID customer_id = getCurrentUserId();
        return service.getUserOrderById(customer_id, orderId);
    }

}
