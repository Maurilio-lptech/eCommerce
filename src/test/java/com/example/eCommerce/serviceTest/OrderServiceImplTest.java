package com.example.eCommerce.serviceTest;

import com.example.eCommerce.dto.OrderDto;
import com.example.eCommerce.entity.Order;
import com.example.eCommerce.entity.User;
import com.example.eCommerce.enums.OrderState;
import com.example.eCommerce.mapper.OrderDetailsMapper;
import com.example.eCommerce.mapper.OrderMapper;
import com.example.eCommerce.repository.OrderRepository;
import com.example.eCommerce.repository.ProductRepository;
import com.example.eCommerce.repository.UserRepository;
import com.example.eCommerce.service.OrderServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Questa annotazione è fondamentale
public class OrderServiceImplTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper mapper;

    @Mock
    private OrderDetailsMapper orderDetailsMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    //test se passo un dto valido per la creazione di un carrello di un utente
    @Test
    void CreateOrder_WithValidDto_ShouldCreateAndReturnOrder() {

        //preparazione dei dati da passare
        // @Order junit
        UUID customerId = UUID.randomUUID();// setto un id customer

        OrderDto inputDto = new OrderDto(); //creo un dto di input
        inputDto.setCustomer_id(customerId);
        inputDto.setState("NEL_CARRELLO"); // stato nel carrello
        inputDto.setOrderDetailsList(Collections.emptyList());

        //mock del customer
        User mockCustomer = new User();
        mockCustomer.setId(customerId);

        //cosa mi aspetto che salvi nel db?
        Order mockSavedOrder = new Order();
        mockSavedOrder.setId(UUID.randomUUID()); // ID ordine come UUID
        mockSavedOrder.setState(OrderState.NEL_CARRELLO);
        mockSavedOrder.setCustomer(mockCustomer);

        //cosa mi aspetto in output?
        OrderDto expectedOutputDto = new OrderDto();
        expectedOutputDto.setId(mockSavedOrder.getId());
        expectedOutputDto.setCustomer_id(customerId);
        expectedOutputDto.setState("NEL_CARRELLO");


        //configurazione del mock
        when(userRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer)); //ritorna un utente
        when(repository.findAllByStateAndCustomerId(OrderState.NEL_CARRELLO, customerId)) // mi ritorna una lista vuota
                .thenReturn(Collections.emptyList());
        when(mapper.toEntity(inputDto)).thenReturn(new Order()); // convertto dto in ordine
        when(repository.save(any(Order.class))).thenReturn(mockSavedOrder); //fingo di salvare l'ordine
        when(mapper.toDto(mockSavedOrder)).thenReturn(expectedOutputDto); // ritorno del dto del ordine salvato

        // eseguo il mock
        OrderDto result = orderService.createOrder(inputDto);

        //verifico i risultati
        assertEquals(mockSavedOrder.getId(), result.getId());
        assertEquals(customerId, result.getCustomer_id());
        assertEquals("NEL_CARRELLO", result.getState());

        verify(userRepository).findById(customerId);
        verify(repository).findAllByStateAndCustomerId(OrderState.NEL_CARRELLO, customerId);
        verify(repository).save(any(Order.class));

    }

    //con un customer che ha già un carrello
    @Test
    void createOrder_WhenCustomerHasExistingCart_ShouldThrowException() {
        // Arrange
        UUID customerId = UUID.randomUUID();

        OrderDto inputDto = new OrderDto();
        inputDto.setCustomer_id(customerId);
        inputDto.setState("NEL_CARRELLO");
        inputDto.setOrderDetailsList(Collections.emptyList());

        User mockCustomer = new User();
        mockCustomer.setId(customerId);

        Order existingOrder = new Order();
        existingOrder.setId(UUID.randomUUID());
        existingOrder.setState(OrderState.NEL_CARRELLO);

        // Configura i mock
        when(userRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(repository.findAllByStateAndCustomerId(OrderState.NEL_CARRELLO, customerId))
                .thenReturn(List.of(existingOrder)); // Simula che esista già un ordine

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(inputDto);
        });

        // Verifiche
        verify(userRepository).findById(customerId);
        verify(repository).findAllByStateAndCustomerId(OrderState.NEL_CARRELLO, customerId);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }

    // con un customer che non esiste
    @Test
    void createOrder_WithNonExistentCustomer_ShouldThrowException() {
        // Arrange
        UUID nonExistentCustomerId = UUID.randomUUID();

        OrderDto invalidDto = new OrderDto();
        invalidDto.setCustomer_id(nonExistentCustomerId);
        invalidDto.setState("NEL_CARRELLO");
        invalidDto.setOrderDetailsList(Collections.emptyList());

        // Configura il mock per restituire Optional vuoto
        when(userRepository.findById(nonExistentCustomerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.createOrder(invalidDto);
        });

        // Verifica che sia stato chiamato findById
        verify(userRepository).findById(nonExistentCustomerId);
        // Verifica che NON siano state chiamate le altre repository
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(repository, mapper);
    }

    //creazione ordine con uno stato non valido
    @Test
    void createOrder_WithInvalidState_ShouldThrowException() {
        // Arrange
        UUID customerId = UUID.randomUUID();

        OrderDto invalidDto = new OrderDto();
        invalidDto.setCustomer_id(customerId);
        invalidDto.setState("STATO_INESISTENTE"); // Stato non valido
        invalidDto.setOrderDetailsList(Collections.emptyList());

        User mockCustomer = new User();
        mockCustomer.setId(customerId);

        // Configura il mock per il customer esistente
        when(userRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(invalidDto);
        });

        // Verifica che sia stato chiamato solo findById
        verify(userRepository).findById(customerId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(repository, mapper);
    }



}
