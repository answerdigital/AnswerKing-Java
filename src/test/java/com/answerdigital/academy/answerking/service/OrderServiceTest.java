package com.answerdigital.academy.answerking.service;

import com.answerdigital.academy.answerking.exception.custom.RetirementException;
import com.answerdigital.academy.answerking.exception.generic.ConflictException;
import com.answerdigital.academy.answerking.exception.generic.NotFoundException;
import com.answerdigital.academy.answerking.model.LineItem;
import com.answerdigital.academy.answerking.model.Product;
import com.answerdigital.academy.answerking.model.Order;
import com.answerdigital.academy.answerking.repository.OrderRepository;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.answerdigital.academy.answerking.request.OrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {OrderService.class})
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private static final long PRODUCT_ID = 1L;
    private static final long ORDER_ID = 2L;

    @Test
    void testAddOrderReturnsSavedOrder() {
        // Given
        OrderRequest orderRequest = new OrderRequest("42 Main St");
        Order expectedResult = Order.builder()
                .address("42 Main Street")
                .build();

        // When
        when(orderRepository.save(any(Order.class)))
                .thenReturn(expectedResult);

        // Then
        assertSame(expectedResult, orderService.addOrder(orderRequest));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testFindByIdReturnsFoundOrder() {
        // Given
        Order order = Order.builder()
                .address("42 Main Street")
                .build();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        // Then
        assertSame(order, orderService.findById(ORDER_ID));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testFindByIdThrowsNotFoundException() {
        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(NotFoundException.class,
                () -> orderService.findById(ORDER_ID));
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
    void testUpdateOrder() {
        // Given
        Order originalOrder = new Order("14 Main St");
        OrderRequest updateOrderRequest = new OrderRequest("14 Green Street");
        Order expectedOrder = new Order("14 Green Street");

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(originalOrder));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(expectedOrder);

        Order response = orderService.updateOrder(ORDER_ID, updateOrderRequest);

        // Then
        assertEquals(expectedOrder, response);
        verify(orderRepository).findById(anyLong());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testUpdateOrderWhenOrderNotExistsThrowsNotFoundException() {
        // Given
        OrderRequest orderRequest = new OrderRequest("14 High St");

        // Then
        assertThrows(NotFoundException.class, () ->
                orderService.updateOrder(ORDER_ID, orderRequest));
    }

    @Test
    void testAddProductToBasketIsSuccessful() {
        // Given (Setup)
        Order order = Order.builder()
                .lineItems(new HashSet<>())
                .build();

        Product product = Product.builder()
                .name("King Burger")
                .description("A burger fit for a king")
                .price(new BigDecimal("12.99"))
                .retired(false)
                .build();

        Order expectedResponse = Order.builder()
                .address("42 Main Street")
                .lineItems(Set.of(new LineItem(order, product, 1)))
                .build();

        // When (Mocking, sending requests)
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(productService.findById(anyLong()))
                .thenReturn(product);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(expectedResponse);

        Order response =
                orderService.addProductToBasket(ORDER_ID, PRODUCT_ID, 1);

        // Then (assertions)
        assertEquals(expectedResponse, response);
        assertFalse(response.getLineItems().isEmpty());
        verify(orderRepository).save(any(Order.class));
        verify(orderRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testAddProductToBasketWhenProductDoesNotExistThrowsNotFoundException() {
        // Given
        Order order = Order.builder()
                .address("42 Main Street")
                .build();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(productService.findById(anyLong()))
                .thenThrow(new NotFoundException("An error occurred"));

        // Then
        assertThrows(NotFoundException.class,
                () -> orderService.addProductToBasket(ORDER_ID, PRODUCT_ID, 2));
        verify(orderRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testAddProductToBasketWhenOrderDoesNotExistThrowsNotFoundException() {
        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(NotFoundException.class,
                () -> orderService.addProductToBasket(ORDER_ID, PRODUCT_ID, 2));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testAddProductToBasketWhenProductIsRetiredThrowsRetirementException() {
        // Given
        Order order = Order.builder()
                .lineItems(new HashSet<>())
                .build();
        Product product = Product.builder()
                .name("King Burger")
                .description("A burger fit for a king")
                .price(new BigDecimal("12.99"))
                .retired(true)
                .build();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(productService.findById(anyLong()))
                .thenReturn(product);

        // Then
        assertThrows(RetirementException.class,
                () -> orderService.addProductToBasket(ORDER_ID, PRODUCT_ID, 1));
        verify(orderRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testAddProductToBasketWhenProductAlreadyExistsInBasketThrowsConflictException() {
        // Given
        Order order = Order.builder()
                .address("42 Main Street")
                .build();
        Product product = Product.builder()
                .name("King Burger")
                .description("A burger fit for a king")
                .price(new BigDecimal("12.99"))
                .retired(false)
                .build();
        order.setLineItems(Set.of(new LineItem(order, product, 1)));

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(productService.findById(anyLong()))
                .thenReturn(product);

        // Then
        assertThrows(ConflictException.class,
                () -> orderService.addProductToBasket(ORDER_ID, PRODUCT_ID, 1));
        verify(orderRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testUpdateProductQuantity() {
        // Given
        Product product = Product.builder()
                .id(12L)
                .name("King Burger")
                .description("A burger fit for a king")
                .price(new BigDecimal("12.99"))
                .retired(false)
                .build();

        Order order = Order.builder()
                .id(12L)
                .address("42 Main Street")
                .build();

        order.setLineItems(Set.of(
                new LineItem(order, product, 1)
        ));

        Order expectedResult = Order.builder()
                .id(12L)
                .address("42 Main Street")
                .build();
        expectedResult.setLineItems(Set.of(
                new LineItem(expectedResult, product, 2)
        ));

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(productService.findById(anyLong()))
                .thenReturn(product);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(expectedResult);

        Order actualResult =
                orderService.updateProductQuantity(ORDER_ID, PRODUCT_ID, 2);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(orderRepository).save(any(Order.class));
        verify(orderRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testUpdateProductQuantityProductNotFoundThrowsNotFoundException() {
        // Given
        Order order = Order.builder()
                .lineItems(new HashSet<>())
                .build();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(productService.findById(anyLong()))
                .thenThrow(new NotFoundException("An error occurred"));

        // Then
        assertThrows(NotFoundException.class,
                () -> orderService.updateProductQuantity(ORDER_ID, PRODUCT_ID, 1));
        verify(orderRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testUpdateProductQuantityOrderNotFoundThrowsNotFoundException() {
        // When
        when(orderRepository.findById(anyLong()))
                .thenThrow(new NotFoundException("An error occurred"));

        // Then
        assertThrows(NotFoundException.class,
                () -> orderService.updateProductQuantity(ORDER_ID, PRODUCT_ID, 1));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testUpdateProductQuantityExistingOrderProductNotPresentThrowsNotFoundException() {
        // Given
        Product product = Product.builder()
                .id(12L)
                .name("King Burger")
                .description("A burger fit for a king")
                .price(new BigDecimal("12.99"))
                .retired(false)
                .build();
        Order order = Order.builder()
                .lineItems(new HashSet<>())
                .build();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(productService.findById(anyLong()))
                .thenReturn(product);

        // Then
        assertThrows(NotFoundException.class,
                () -> orderService.updateProductQuantity(ORDER_ID, PRODUCT_ID, 2));
        verify(orderRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testDeleteProductInBasket() {
        // Given
        Product product = Product.builder()
                .id(12L)
                .build();

        Order order = Order.builder()
                .id(12L)
                .address("42 Main Street")
                .lineItems(new HashSet<>())
                .build();
        LineItem lineItem = LineItem.builder()
                .order(order)
                .product(product)
                .quantity(5)
                .build();
        order.getLineItems().add(lineItem);

        Order expectedResponse = Order.builder()
                .id(12L)
                .build();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(productService.findById(anyLong()))
                .thenReturn(product);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(expectedResponse);

        Order response = orderService.deleteProductInBasket(ORDER_ID, PRODUCT_ID);

        // Then
        assertEquals(expectedResponse, response);
        verify(orderRepository).findById(anyLong());
        verify(productService).findById(anyLong());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testDeleteProductInBasketWhenOrderNotFoundThrowsNotFoundException() {
        // When
        when(orderRepository.findById(anyLong()))
                .thenThrow(new NotFoundException("An error occurred."));

        // Then
        assertThrows(NotFoundException.class, () ->
                orderService.deleteProductInBasket(ORDER_ID, PRODUCT_ID));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testDeleteProductInBasketWhenProductNotFoundThrowsNotFoundException() {
        // Given
        Order order = Order.builder()
                .lineItems(new HashSet<>())
                .build();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(productService.findById(anyLong()))
                .thenThrow(new NotFoundException("An error occurred"));

        // Then
        assertThrows(NotFoundException.class, () ->
                orderService.deleteProductInBasket(ORDER_ID, PRODUCT_ID));
        verify(orderRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testDeleteProductInBasketWhenExistingOrderProductIsEmptyThrowNotFoundException() {
        // Given
        Product product = Product.builder().build();
        Order order = Order.builder()
                .lineItems(new HashSet<>())
                .build();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(productService.findById(anyLong()))
                .thenReturn(product);

        // Then
        assertThrows(NotFoundException.class, () ->
                orderService.deleteProductInBasket(ORDER_ID, PRODUCT_ID));
        verify(orderRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }
}
