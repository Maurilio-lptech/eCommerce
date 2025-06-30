package com.example.eCommerce.controller;

import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.securitySpring.security.services.UserDetailsImpl;
import com.example.eCommerce.serviceTest.CartServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartServiceImpl cartService;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        objectMapper = new ObjectMapper();
        testUserId = UUID.randomUUID();

        // Mock authentication
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(testUserId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    //test controller get cart
    @Test
    void getCart_ShouldReturnCart() throws Exception {
        OrderDto expectedCart = new OrderDto(); //creo un dto  per il carello che mi aspetto
        when(cartService.getCart(testUserId)).thenReturn(expectedCart); // quando passo un customer id mi aspetto che mi ritorna il carello se prensente nel db

        mockMvc.perform(get("/api/cart/"))// simulo una richiesta get con mockMvc
                .andExpect(status().isOk()) // quando lo stato della chiamata è ok
                .andExpect(content().json(objectMapper.writeValueAsString(expectedCart))); // mi aspetto un carello

        verify(cartService).getCart(testUserId); // verifico la condizione
    }

    @Test
    void createCart_ShouldCreateNewCart() throws Exception {
        OrderDto newCart = new OrderDto(); // order dto
        when(cartService.createCart(testUserId)).thenReturn(newCart); // quando passo un id utente mi aspetto un ritorno di  nuovo carello

        mockMvc.perform(post("/api/cart/new"))
                .andExpect(status().isCreated()) // quando il response entity restituisce CREATED
                .andExpect(content().json(objectMapper.writeValueAsString(newCart))); // mi aspetto che ritorni un orderDto che rapressenta il nuovo carello

        verify(cartService).createCart(testUserId);
    }

    @Test
    void addToCart_ShouldAddItem() throws Exception {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
        OrderDto updatedCart = new OrderDto();

        when(cartService.addToCart(eq(testUserId), any(OrderDetailsDto.class))).thenReturn(updatedCart);

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDetailsDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedCart)));

        verify(cartService).addToCart(eq(testUserId), any(OrderDetailsDto.class));
    }

    @Test
    void removeFromCart_ShouldRemoveItem() throws Exception {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
        OrderDto updatedCart = new OrderDto();

        when(cartService.removeFromCart(eq(testUserId), any(OrderDetailsDto.class))).thenReturn(updatedCart);

        mockMvc.perform(post("/api/cart/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDetailsDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedCart)));

        verify(cartService).removeFromCart(eq(testUserId), any(OrderDetailsDto.class));
    }

    @Test
    void clearCart_ShouldClearCart() throws Exception {
        doNothing().when(cartService).clearCart(testUserId);

        mockMvc.perform(post("/api/cart/clear"))
                .andExpect(status().isNoContent());

        verify(cartService).clearCart(testUserId);
    }

    @Test
    void checkout_ShouldProcessCheckout() throws Exception {
        OrderDto cart = new OrderDto();
        OrderDto processedOrder = new OrderDto();

        when(cartService.checkout(any(OrderDto.class))).thenReturn(processedOrder);

        mockMvc.perform(post("/api/cart/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(processedOrder)));

        verify(cartService).checkout(any(OrderDto.class));
    }
}