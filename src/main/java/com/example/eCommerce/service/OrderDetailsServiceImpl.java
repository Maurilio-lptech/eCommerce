package com.example.eCommerce.service;

import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.entity.Order;
import com.example.eCommerce.entity.OrderDetails;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.enums.OrderState;
import com.example.eCommerce.mapper.OrderDetailsMapper;
import com.example.eCommerce.repository.OrderDetailsRepository;
import com.example.eCommerce.repository.OrderRepository;
import com.example.eCommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements  OrderDetailsService{

    private final OrderDetailsRepository repository;
    private final OrderDetailsMapper mapper;
    private final OrderRepository orderRepository; // cosa è meglio che si usa il sevice o un repository?
    private final ProductRepository productRepository;

    //CRUD
    public OrderDetailsDto createOrderDetails(OrderDetailsDto orderDetailsDto){

        // Verifico se ordine e prodotto sono presenti nel db
        Order order= orderRepository.findById(orderDetailsDto.getOrder_id())
                .orElseThrow(()-> new EntityNotFoundException("Ordine con id "+orderDetailsDto.getOrder_id()+" non trovato nel DB"));

        Product product= productRepository.findById(orderDetailsDto.getProduct_id())
                .orElseThrow(()-> new EntityNotFoundException("Prodotto con id "+orderDetailsDto.getProduct_id()+" non trovato nel DB"));

        OrderDetails newOrderDetails= mapper.toEntity(orderDetailsDto);

        newOrderDetails.setOrder(order);
        newOrderDetails.setProduct(product);
        newOrderDetails.setPriceForUnit(product.getPrice());

        return mapper.toDto(repository.save(newOrderDetails));
    }

    public void deleteOrderDetails(UUID id){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Dettaglio ordine con id "+id+" non trovato nel DB");
        }

        repository.deleteById(id);
    }

    public OrderDetailsDto updateOrderDetails(UUID id, OrderDetailsDto orderDetailsDto){
        // Verifico se ordine e prodotto sono presenti nel db
        OrderDetails orderDetails= repository.findById(orderDetailsDto.getId())
                .orElseThrow(()-> new EntityNotFoundException("Ordine Dettagliato con id"+orderDetailsDto.getId()+" non trovato nel DB"));

        Order order= orderRepository.findById(orderDetailsDto.getOrder_id())
                .orElseThrow(()-> new EntityNotFoundException("Ordine con id "+orderDetailsDto.getOrder_id()+" non trovato nel DB"));

        Product product= productRepository.findById(orderDetailsDto.getOrder_id())
                .orElseThrow(()-> new EntityNotFoundException("Prodotto con id "+orderDetailsDto.getProduct_id()+" non trovato nel DB"));


        OrderDetails updatedOrderDetails= mapper.toEntity(orderDetailsDto);
        updatedOrderDetails.setOrder(order);
        updatedOrderDetails.setProduct(product);

        return mapper.toDto(repository.save(updatedOrderDetails));
    }

    public OrderDetailsDto getOrderDetailsById(UUID id){
        return mapper.toDto(repository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("OrderDetails con id "+id+" non trovato nel db")));
    }

    public Page<OrderDetailsDto> getAllOrderDetails(Pageable pageable){
        //trovo tutti gli order details e li converto in dto;
        return repository.findAll(pageable).map(mapper::toDto);
    }

    //OTHER
    public List<OrderDetailsDto> getAllOrderDetailsByOrderId(UUID orderId){
        //trovo tutti gli order details di un order e li converto in dto;
        return repository.findAllByOrderId(orderId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    //CART
    public List<OrderDetailsDto> getAllCartOrderDetails(){

        return repository.findAllByOrderState(OrderState.NEL_CARRELLO)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


}
