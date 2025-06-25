package com.example.eCommerce.service;

import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.entity.Order;
import com.example.eCommerce.entity.OrderDetails;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.entity.User;
import com.example.eCommerce.enums.OrderState;
import com.example.eCommerce.mapper.OrderDetailsMapper;
import com.example.eCommerce.mapper.OrderMapper;
import com.example.eCommerce.repository.OrderRepository;
import com.example.eCommerce.repository.ProductRepository;
import com.example.eCommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;
    private final OrderDetailsMapper orderDetailsMapper;


    public OrderDto getOrderById(UUID id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nessun utente trovato con id" + id)));
    }

    @Transactional
    public OrderDto createOrder(@NotNull OrderDto orderDto) {

        // Verifica che il cliente esista
        if (orderDto.getCustomer_id() == null) {
            throw new IllegalArgumentException("ïnserisci un id del utente per creare un ordine");
        }

        User customer = userRepository.findById(orderDto.getCustomer_id())
                .orElseThrow(() -> new EntityNotFoundException("Cliente con ID " + orderDto.getCustomer_id() + " non trovato"));

        boolean isCart = false;
        if (orderDto.getState().equals("NEL_CARRELLO")) {
            isCart = true;
            List<Order> existingCart = repository.findAllByStateAndCustomerId(OrderState.NEL_CARRELLO, orderDto.getCustomer_id());
            if (!existingCart.isEmpty()) {
                throw new IllegalArgumentException("Esiste già un carrello per questo utente");
            }
        }

        // Validazione iniziale
        if (orderDto.getId() != null) {
            throw new IllegalArgumentException("L'ID deve essere null per una nuova creazione");
        }

        if ((orderDto.getOrderDetailsList() == null || orderDto.getOrderDetailsList().isEmpty()) && !isCart) {
            throw new IllegalArgumentException("L'ordine deve contenere almeno un prodotto");
        }


        // Converti DTO in entità
        Order order = mapper.toEntity(orderDto);
        order.setCustomer(customer);

        // Imposta lo stato iniziale (se non specificato)
        if (order.getState() == null) {
            order.setState(OrderState.NEL_CARRELLO);
        } else {
            switch (orderDto.getState().trim().toUpperCase()) {
                case "NEL_CARRELLO":
                    order.setState(OrderState.NEL_CARRELLO);
                    break;

                case "IN_ELABORAZIONE":
                    order.setState(OrderState.IN_ELABORAZIONE);
                    order.setOrderDate(new Date());
                    break;

                case "SPEDITO":
                    order.setState(OrderState.SPEDITO);
                    break;

                case "CONSEGNATO":
                    order.setState(OrderState.CONSEGNATO);
                    break;

                case "ANNULATO":
                    order.setState(OrderState.ANNULATO);
                    break;
                default:
                    throw new IllegalArgumentException("Stato ordine " + order.getState() + " non valido");

            }
        }


        // ogni prodotto che ordina devo creare l'orderd details singolarmente
        //prendo dalla lista
        //imposto id cliente
        //imposto id ordine

        //creo una lista per le entita da aggiungere
        if (!isCart) {
            List<OrderDetails> orderDetailsEntities = new ArrayList<>();

            for (OrderDetailsDto detailsDto : orderDto.getOrderDetailsList()) {
                OrderDetails orderDetails = orderDetailsMapper.toEntity(detailsDto);

                //verifico che esista il prodotto
                Product product = productRepository.findById(detailsDto.getProduct_id())
                        .orElseThrow(() -> new EntityNotFoundException("Id prodotto" + detailsDto.getProduct_id() + " non trovato"));


                orderDetails.setOrder(order);
                orderDetails.setProduct(product);
                orderDetails.setPriceForUnit(product.getPrice()); // recupero il prezzo per ogni prodotto
                orderDetailsEntities.add(orderDetails);

            }

            // imposto la lista di orderDetails
            order.setOrderDetailsList(orderDetailsEntities);
        }
        // Salva l'ordine (cascade salverà anche gli OrderDetails)
        Order savedOrder = repository.save(order);

        return mapper.toDto(savedOrder);
    }


    @Transactional
    public OrderDto updateOrder(@NotNull OrderDto orderDto) {
        // Validazioni iniziali
        if (orderDto.getId() == null) {
            throw new IllegalArgumentException("ID ordine mancante per l'aggiornamento");
        }

        // Recupera l'ordine esistente
        Order existingOrder = repository.findById(orderDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Ordine con ID " + orderDto.getId() + " non trovato"));

        // Verifica che il cliente esista
        if (orderDto.getCustomer_id() == null) {
            throw new IllegalArgumentException("Inserisci un ID utente per aggiornare l'ordine");
        }

        User customer = userRepository.findById(orderDto.getCustomer_id())
                .orElseThrow(() -> new EntityNotFoundException("Cliente con ID " + orderDto.getCustomer_id() + " non trovato"));

        // Validazione stato carrello
        boolean isCart = false;
        if (orderDto.getState().equals("NEL_CARRELLO")) {
             isCart = true;
            List<Order> existingCart = repository.findAllByStateAndCustomerId(OrderState.NEL_CARRELLO, orderDto.getCustomer_id());
            if (!existingCart.isEmpty() && !existingCart.get(0).getId().equals(orderDto.getId())) {
                throw new IllegalArgumentException("Esiste già un carrello per questo utente");
            }
        }

        // Validazione prodotti
        if (orderDto.getOrderDetailsList() == null || orderDto.getOrderDetailsList().isEmpty() && !isCart) {
            throw new IllegalArgumentException("L'ordine deve contenere almeno un prodotto");
        }

        // Converti DTO in entità
        Order order = mapper.toEntity(orderDto);
        order.setCustomer(customer);
        // Gestione stato ordine
        switch (orderDto.getState().trim().toUpperCase()) {
            case "NEL_CARRELLO":
                order.setState(OrderState.NEL_CARRELLO);
                break;
            case "IN_ELABORAZIONE":
                order.setState(OrderState.IN_ELABORAZIONE);
                order.setOrderDate(new Date());
                break;
            case "SPEDITO":
                order.setState(OrderState.SPEDITO);
                break;
            case "CONSEGNATO":
                order.setState(OrderState.CONSEGNATO);
                break;
            case "ANNULATO":
                order.setState(OrderState.ANNULATO);
                break;
            default:
                throw new IllegalArgumentException("Stato ordine " + order.getState() + " non valido");
        }

        // Gestione OrderDetails
        List<OrderDetails> orderDetailsEntities = new ArrayList<>();
        for (OrderDetailsDto detailsDto : orderDto.getOrderDetailsList()) {
            OrderDetails orderDetails = orderDetailsMapper.toEntity(detailsDto);

            Product product = productRepository.findById(detailsDto.getProduct_id())
                    .orElseThrow(() -> new EntityNotFoundException("Id prodotto " + detailsDto.getProduct_id() + " non trovato"));

            orderDetails.setOrder(order);
            orderDetails.setProduct(product);
            orderDetails.setPriceForUnit(product.getPrice());
            orderDetailsEntities.add(orderDetails);
        }

        // Imposta la lista aggiornata di orderDetails
        order.setOrderDetailsList(orderDetailsEntities);


        // Salva l'ordine aggiornato
        Order savedOrder = repository.save(order);

        return mapper.toDto(savedOrder);
    }

    @Transactional
    public void deleteOrder(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Id non trovato nel DB");
        }

        repository.deleteById(id);
    }

    public Page<OrderDto> getAllOrders(Pageable pageable) {
        Page<Order> orderPage = repository.findAll(pageable);

        return orderPage.map(mapper::toDto);
    }

    public List<OrderDto> getAllOrdersByState(String state) {

        // CONSIGLIATO DA INTELLIJ
        OrderState orderState = switch (state.trim().toUpperCase()) {
            case "NEL_CARRELLO" -> OrderState.NEL_CARRELLO;
            case "IN_ELABORAZIONE" -> OrderState.IN_ELABORAZIONE;
            case "SPEDITO" -> OrderState.SPEDITO;
            case "CONSEGNATO" -> OrderState.CONSEGNATO;
            case "ANNULATO" -> OrderState.ANNULATO;
            default -> throw new IllegalArgumentException("Stato ordine " + state + " non valido");
        };

        return repository.findAllByState(orderState).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());


    }

    public OrderDto getCart(UUID customerId) {
        Order cart = repository.findByStateAndCustomerId(OrderState.NEL_CARRELLO, customerId).orElseThrow(() -> new EntityNotFoundException("L'utente non ha un carrello"));
        return mapper.toDto(cart);
    }



}
