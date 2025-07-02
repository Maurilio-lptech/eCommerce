package com.example.eCommerce.controllerTest;

import com.example.eCommerce.controller.CartController;
import com.example.eCommerce.dto.OrderDetailsDto;
import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.securitySpring.security.services.UserDetailsImpl;
import com.example.eCommerce.service.CartServiceImpl;
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

    //creo un mock del service
    @Mock
    private CartServiceImpl cartService;

    //inietto il service nel controller
    @InjectMocks
    private CartController cartController;

    //mockMvc mi permette di simulare chiamate http al controller e verificare la risposta
    private MockMvc mockMvc;
    //mi permette di convertire da ogetti java a json e viceversa
    private ObjectMapper objectMapper;
    //id di test
    private UUID testUserId;

    //prima di ogni test eseguo
    @BeforeEach
    void setUp() {
        //inizializzo il mock in standalone senza avviare il server
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        // creo il mapper
        objectMapper = new ObjectMapper();
        //genero un id per i test
        testUserId = UUID.randomUUID();


        // Crea un mock dell'oggetto UserDetailsImpl (che rappresenta l'utente autenticato).
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(testUserId);

        // Mock authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    //test controller get cart
    @Test
    void getCart_ShouldReturnCart() throws Exception {
        OrderDto expectedCart = new OrderDto(); //creo un dto  per il carrello che mi aspetto
        when(cartService.getCart(testUserId)).thenReturn(expectedCart); // quando passo un customer id mi aspetto che mi ritorna il carrello se prensente nel db

        mockMvc.perform(get("/api/cart/"))// simulo una richiesta get con mockMvc
                .andExpect(status().isOk()) // quando lo stato della chiamata è ok
                .andExpect(content().json(objectMapper.writeValueAsString(expectedCart))); // mi aspetto un carrello

        verify(cartService).getCart(testUserId); // verifico la condizione
    }

    //creazione di un carrello se non esiste un carrello un carrello di un utente
    @Test
    void createCart_ShouldCreateNewCart() throws Exception {
        OrderDto newCart = new OrderDto(); // order dto
        when(cartService.createCart(testUserId)).thenReturn(newCart); // quando passo un id utente mi aspetto un ritorno di  nuovo carrello

        mockMvc.perform(post("/api/cart/new"))
                .andExpect(status().isCreated()) // quando il response entity restituisce CREATED
                .andExpect(content().json(objectMapper.writeValueAsString(newCart))); // mi aspetto che ritorni un orderDto che rapressenta il nuovo carrello

        verify(cartService).createCart(testUserId);
    }

    //passando un orderDetaisDto valido mi aspetto che restiuisca il carrello del utente con almeno un ordine dto aggiunto
    @Test
    void addToCart_ShouldAddItem() throws Exception {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
        OrderDto updatedCart = new OrderDto();

        when(cartService.addToCart(eq(testUserId), any(OrderDetailsDto.class))).thenReturn(updatedCart);
        //quando viene passato un idUtente e un qualsiasi orderDetailsDto dovrebbe ritornare un carrello aggiornato

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON) // simulo una chiamata con passagio di un json
                        .content(objectMapper.writeValueAsString(orderDetailsDto))) //converto ogetto java in stringa json
                .andExpect(status().isOk()) // quando stato ok
                .andExpect(content().json(objectMapper.writeValueAsString(updatedCart))); //restituisco un cart aggiornato

        verify(cartService).addToCart(eq(testUserId), any(OrderDetailsDto.class));
        //inizio verifica su cartservice
        // verifica che il primo parametro sia uguale a userid
    }

    //rimovere un prodotto da un carrello e mi aspetto un carrello aggiornato
    //procedimento simile al precedente
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

    //puluzia di un carrello mi aspetto che mandando un id utente mi pulisca il carrello e non restituisca niente al controller
    //la risposta che mi aspetto è un no content
    @Test
    void clearCart_ShouldClearCart() throws Exception {
        doNothing().when(cartService).clearCart(testUserId);

        mockMvc.perform(post("/api/cart/clear"))
                .andExpect(status().isNoContent());

        verify(cartService).clearCart(testUserId);
    }

    //durante il chekout mi aspetto di passare un order dto
    // mi ritorna un ordder dto processato "in_elaborazione"
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