package com.answerdigital.benhession.academy.answerkingweek2.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ItemUnavailableException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.model.OrderItem;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.OrderRepository;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {OrderService.class})
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testAddOrderReturnsSavedOrder() {
        // Given
        Order order = new Order("42 Main St");

        // When
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        // Then
        assertSame(order, orderService.addOrder("42 Main St"));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testFindByIdReturnsFoundOrder() {
        // Given
        Order order = new Order("42 Main St");

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        // Then
        assertSame(order, orderService.findById(123L));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testFindByIdThrowsNotFoundException() {
        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(NotFoundException.class,
                () -> orderService.findById(123L));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testFindAllReturnsEmptyListOfOrders() {
        // Given
        List<Order> orders = List.of();

        // When
        when(orderRepository.findAll())
                .thenReturn(orders);

        List<Order> response = orderService.findAll();

        // Then
        assertSame(orders, response);
        assertTrue(response.isEmpty());
        verify(orderRepository).findAll();
    }

    @Test
    void testFindAllReturnsListOfOrders() {
        // Given
        List<Order> orders = List.of(
                new Order("14 Green Street"),
                new Order("21 1/2 Argument Street"),
                new Order("Flat 2, 24B Oswald Street")
        );

        // When
        when(orderRepository.findAll())
                .thenReturn(orders);

        List<Order> actualFindAllResult = orderService.findAll();

        // Then
        assertSame(orders, actualFindAllResult);
        assertFalse(actualFindAllResult.isEmpty());
        verify(orderRepository).findAll();
    }

    @Test
    void testAddItemToBasketIsSuccessful() {
        // Given (Setup)
        Order order = new Order();
        Item item = new Item("King Burger", "A burger fit for a king.", new BigDecimal("12.99"), true);
        Order expectedResponse = new Order("42 Main St");
        expectedResponse.setOrderItems(Set.of(new OrderItem(order, item, 1)));

        // When (Mocking, sending requests)
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(itemService.findById(anyLong()))
                .thenReturn(item);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(expectedResponse);

        Order response =
                orderService.addItemToBasket(12L, 12L, 1);

        // Then (assertions)
        assertEquals(expectedResponse, response);
        assertFalse(response.getOrderItems().isEmpty());
        verify(orderRepository).save(any(Order.class));
        verify(orderRepository).findById(anyLong());
        verify(itemService).findById(anyLong());
    }

    @Test
    void testAddItemToBasketWhenItemDoesNotExistThrowsNotFoundException() {
        // Given
        Order order = new Order("42 Main St");

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(itemService.findById(anyLong()))
                .thenThrow(new NotFoundException("An error occurred"));

        // Then
        assertThrows(NotFoundException.class,
                () -> orderService.addItemToBasket(123L, 123L, 2));
        verify(orderRepository).findById(anyLong());
        verify(itemService).findById(anyLong());
    }

    @Test
    void testAddItemToBasketWhenOrderDoesNotExistThrowsNotFoundException() {
        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(NotFoundException.class,
                () -> orderService.addItemToBasket(123L, 123L, 2));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testAddItemToBasketWhenItemIsUnavailableThrowsItemUnavailableException() {
        // Given
        Order order = new Order();
        Item item = new Item("King Burger", "A burger fit for a king.", new BigDecimal("12.99"), false);

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(itemService.findById(anyLong()))
                .thenReturn(item);

        // Then
        assertThrows(ItemUnavailableException.class,
                () -> orderService.addItemToBasket(123L, 123L, 1));
        verify(orderRepository).findById(anyLong());
        verify(itemService).findById(anyLong());
    }

    @Test
    void testAddItemToBasketWhenItemAlreadyExistsInBasketThrowsConflictException() {
        // Given
        Order order = new Order("42 Main St");
        Item item = new Item("King Burger", "A burger fit for a king.", new BigDecimal("12.99"), true);
        order.setOrderItems(Set.of(new OrderItem(order, item, 1)));

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(itemService.findById(anyLong()))
                .thenReturn(item);

        // Then
        assertThrows(ConflictException.class,
                () -> orderService.addItemToBasket(12L, 12L, 1));
        verify(orderRepository).findById(anyLong());
        verify(itemService).findById(anyLong());
    }

    @Test
    void testUpdateItemQuantity() {
        // Given
        Item item = new Item("King Burger", "A burger fit for a king", new BigDecimal("12.99"), true);
        item.setId(12L);

        Order order = new Order("42 Main St");
        order.setId(12L);
        order.setOrderItems(Set.of(
                new OrderItem(order, item, 1)
        ));

        Order expectedResult = new Order("42 Main St");
        expectedResult.setId(12L);
        expectedResult.setOrderItems(Set.of(
                new OrderItem(expectedResult, item, 2)
        ));

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(itemService.findById(anyLong()))
                .thenReturn(item);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(expectedResult);

        Order actualResult =
                orderService.updateItemQuantity(12L, 12L, 2);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(orderRepository).save(any(Order.class));
        verify(orderRepository).findById(anyLong());
        verify(itemService).findById(anyLong());
    }

    @Test
    void testUpdateItemQuantityItemNotFoundThrowsNotFoundException() {
        // Given
        Order order = new Order();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(itemService.findById(anyLong()))
                .thenThrow(new NotFoundException("An error occurred"));

        // Then
        assertThrows(NotFoundException.class,
                () -> orderService.updateItemQuantity(12L, 12L, 1));
        verify(orderRepository).findById(anyLong());
        verify(itemService).findById(anyLong());
    }

    @Test
    void testUpdateItemQuantityOrderNotFoundThrowsNotFoundException() {
        // When
        when(orderRepository.findById(anyLong()))
                .thenThrow(new NotFoundException("An error occurred"));

        // Then
        assertThrows(NotFoundException.class,
                () -> orderService.updateItemQuantity(12L, 12L, 1));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testUpdateItemQuantityExistingOrderItemNotPresentThrowsNotFoundException() {
        // Given
        Item item = new Item("King Burger", "A burger fit for a king", new BigDecimal("12.99"), true);
        Order order = new Order();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(itemService.findById(anyLong()))
                .thenReturn(item);

        // Then
        assertThrows(NotFoundException.class,
                () -> orderService.updateItemQuantity(12L, 12L, 2));
        verify(orderRepository).findById(anyLong());
        verify(itemService).findById(anyLong());
    }

    @Test
    void testDeleteItemInBasket() {
        // Given
        Item item = new Item();
        item.setId(12L);

        Order order = new Order("12 Brown Street");
        order.setId(12L);
        OrderItem orderItem = new OrderItem(order, item, 5);
        order.getOrderItemsSet().add(orderItem);

        Order expectedResponse = new Order();
        expectedResponse.setId(12L);

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(itemService.findById(anyLong()))
                .thenReturn(item);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(expectedResponse);

        Order response = orderService.deleteItemInBasket(12L, 12L);

        // Then
        assertEquals(expectedResponse, response);
        verify(orderRepository).findById(anyLong());
        verify(itemService).findById(anyLong());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testDeleteItemInBasketWhenOrderNotFoundThrowsNotFoundException() {
        // When
        when(orderRepository.findById(anyLong()))
                .thenThrow(new NotFoundException("An error occurred."));

        // Then
        assertThrows(NotFoundException.class, () ->
                orderService.deleteItemInBasket(12L, 12L));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testDeleteItemInBasketWhenItemNotFoundThrowsNotFoundException() {
        // Given
        Order order = new Order();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(itemService.findById(anyLong()))
                .thenThrow(new NotFoundException("An error occurred"));

        // Then
        assertThrows(NotFoundException.class, () ->
                orderService.deleteItemInBasket(12L, 12L));
        verify(orderRepository).findById(anyLong());
        verify(itemService).findById(anyLong());
    }

    @Test
    void testDeleteItemInBasketWhenExistingOrderItemIsEmptyThrowNotFoundException() {
        // Given
        Item item = new Item();
        Order order = new Order();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(itemService.findById(anyLong()))
                .thenReturn(item);

        // Then
        assertThrows(NotFoundException.class, () ->
                orderService.deleteItemInBasket(12L, 12L));
        verify(orderRepository).findById(anyLong());
        verify(itemService).findById(anyLong());
    }
}