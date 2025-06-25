package com.example.eCommerce.service;

import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.dto.ProductDto;
import com.example.eCommerce.enums.OrderState;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final OrderService orderService;
    private final ProductService productService;
    //TODO: remuovi la quantita

    //  Crea un nuovo carrello per un utente
    public OrderDto createCart(UUID customerId) {
        OrderDto cart = new OrderDto();
        cart.setCustomer_id(customerId);
        cart.setOrderDetailsList(new ArrayList<>());
        cart.setState(OrderState.NEL_CARRELLO.toString());
        return orderService.createOrder(cart);
    }

    //  Aggiungi prodotto al carrello
    @Transactional
    public OrderDto addToCart(UUID customerId, OrderDetailsDto orderDetailsDto) {

        UUID productId = orderDetailsDto.getProduct_id();
        int quantity = orderDetailsDto.getQuantity();
        // Trova o crea il carrello
        OrderDto cart = findOrCreateCart(customerId);

        // Verifica prodotto e quantità
        ProductDto product = productService.getProductById(productId);
        if (quantity <= 0 || product.getQuantity_available() < quantity) {
            throw new IllegalArgumentException("Quantità non valida o prodotto esaurito");
        }

        // verifico se il prodotto non è nel carrello
        boolean productExistsInCart = false;
        for (OrderDetailsDto item : cart.getOrderDetailsList()) {
            if (item.getProduct_id().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                productExistsInCart = true;
                break;
            }
        }

        //se il prodotto è già nel carello
        if (!productExistsInCart) {
            OrderDetailsDto newItem = new OrderDetailsDto();
            newItem.setProduct_id(productId);
            newItem.setQuantity(quantity);
            newItem.setPriceForUnit(product.getPrice());
            cart.getOrderDetailsList().add(newItem);
        }

        return orderService.updateOrder(cart);
    }

    //Rimuovi prodotto dal carrello
    @Transactional
    public OrderDto removeFromCart(UUID customerId, OrderDetailsDto orderDetailsDto) {

        UUID productId = orderDetailsDto.getProduct_id();
        int quantity = orderDetailsDto.getQuantity();

        // Trova o crea il carrello
        OrderDto cart = findOrCreateCart(customerId);

        // Verifica prodotto e quantità
        ProductDto product = productService.getProductById(productId);
        if (quantity <= 0 || product.getQuantity_available() < quantity) {
            throw new IllegalArgumentException("Quantità non valida o prodotto esaurito");
        }


        // verifico se il prodotto non è nel carrello
        boolean productExistsInCart = false;
        for (OrderDetailsDto item : cart.getOrderDetailsList()) {
            if (item.getProduct_id().equals(productId)) {
                if (item.getQuantity() - quantity < 0) {
                    item.setQuantity(0);
                }else {
                    item.setQuantity(item.getQuantity() - quantity);
                }
                productExistsInCart = true;
                break;
            }
        }

        //se il prodotto è già nel carello
        if (!productExistsInCart) {
            throw new IllegalArgumentException("prodotto non trovato nel carello");
        }


        return orderService.updateOrder(cart);
    }

    // Svuota il carrello
    public void clearCart(UUID customerId) {
        OrderDto cart = orderService.getCart(customerId);
        cart.getOrderDetailsList().clear();
        orderService.updateOrder(cart);
    }

    // Checkout (converte carrello in ordine)
    public OrderDto checkout(UUID customerId) {
        OrderDto cart = orderService.getCart(customerId);
        cart.setState(OrderState.IN_ELABORAZIONE.toString());
        return orderService.updateOrder(cart);
    }

    public OrderDto getCart(UUID customerId){
        return orderService.getCart(customerId);
    }

    //metodo per controllare se c'è un carello altrimenti lo creo
    private OrderDto findOrCreateCart(UUID customerId) {
        List<OrderDto> carts = orderService.getAllOrdersByState(OrderState.NEL_CARRELLO.toString())
                .stream()
                .filter(o -> o.getCustomer_id().equals(customerId))
                .toList();

        return carts.isEmpty() ? createCart(customerId) : carts.get(0);
    }
}