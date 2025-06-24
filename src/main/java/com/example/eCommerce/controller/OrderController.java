package com.example.eCommerce.controller;
import com.example.eCommerce.dto.CategoryDto;
import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService service;

    @PostMapping("/new")
    public ResponseEntity<OrderDto> createCategory(@RequestBody @NotNull OrderDto EntityToCreate){
        if(EntityToCreate.getId()!=null){
            EntityToCreate.setId(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(EntityToCreate));
    }

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Void> deleteOrderDetails(@PathVariable  UUID id){
//        service.deleteOrderDetails(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<OrderDetailsDto> getOrderDetailsById(@PathVariable  UUID id){
//        return ResponseEntity.ok(service.getOrderDetailsById(id));
//    }

    @GetMapping("/AllOrder")
    public Page<OrderDto> findAllOrder(Pageable pageable){
        return service.getAllOrders(pageable);
    }

    @GetMapping("/AllOrder/{state}")
    public List<OrderDto> findAllOrderByState(@PathVariable String state){
        return service.getAllOrdersByState(state);
    }


}
