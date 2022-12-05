package com.answerdigital.answerking.service;

import com.answerdigital.answerking.builder.OrderRequestTestBuilder;
import com.answerdigital.answerking.builder.OrderTestBuilder;
import com.answerdigital.answerking.exception.custom.OrderCancelledException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.mapper.OrderMapper;
import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.model.OrderStatus;
import com.answerdigital.answerking.repository.OrderRepository;

import com.answerdigital.answerking.request.OrderRequest;
import com.answerdigital.answerking.response.OrderResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@ContextConfiguration(classes = {OrderService.class})
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private final OrderTestBuilder orderTestBuilder = new OrderTestBuilder();
    private final OrderRequestTestBuilder orderRequestTestBuilder = new OrderRequestTestBuilder();

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    private static final Long NONEXISTENT_ORDER_ID = 10L;

    @Test
    void testAddOrderWithNoProductsValidOrderRequestIsSuccessful() {
        // Given
        Order order = orderTestBuilder
            .withDefaultValues()
            .build();
        OrderRequest orderRequest = orderRequestTestBuilder
            .withDefaultValues()
            .build();

        // When
        doReturn(order)
            .when(orderRepository)
            .save(any(Order.class));

        OrderResponse response = orderService.addOrder(orderRequest);

        // Then
        assertEquals(OrderStatus.CREATED, response.getOrderStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testFindByIdReturnsFoundOrder() {
        // Given
        final Order order = Order.builder()
                .address("42 Main Street")
                .build();

    void testFindByIdWithInvalidIdThrowsNotFoundException() {
        // When
        doReturn(Optional.empty())
            .when(orderRepository)
            .findById(anyLong());

        // Then
        assertThrows(NotFoundException.class, () -> orderService.getOrderResponseById(NONEXISTENT_ORDER_ID));
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
        final List<Order> orders = List.of();

        // When
        when(orderRepository.findAll())
                .thenReturn(orders);

        final List<Order> response = orderService.findAll();

        // Then
        assertSame(orders, response);
        assertTrue(response.isEmpty());
        verify(orderRepository).findAll();
    }

    @Test
    void testFindAllReturnsListOfOrders() {
        // Given
        final List<Order> orders = List.of(
            orderTestBuilder.withId(1L).build(),
            orderTestBuilder.withId(2L).build()
        );

        // When
        doReturn(orders)
            .when(orderRepository)
            .findAll();

        List<OrderResponse> response = orderService.findAll();

        // Then
        assertEquals(2, response.size());
        assertFalse(response.isEmpty());
        verify(orderRepository).findAll();
    }

    @Test
    void testUpdateOrder() {
        // Given
        final Order originalOrder = new Order("14 Main St");
        final OrderRequest updateOrderRequest = new OrderRequest("14 Green Street");
        final Order expectedOrder = new Order("14 Green Street");

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(originalOrder));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(expectedOrder);

        final Order response = orderService.updateOrder(ORDER_ID, updateOrderRequest);

        // Then
        assertEquals(expectedOrder, response);
        verify(orderRepository).findById(anyLong());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testUpdateOrderWhenOrderNotExistsThrowsNotFoundException() {
        // Given
        final OrderRequest orderRequest = new OrderRequest("14 High St");

        // Then
        assertThrows(NotFoundException.class, () ->
                orderService.updateOrder(ORDER_ID, orderRequest));
    }

    @Test
    void testAddProductToBasketIsSuccessful() {
        // Given (Setup)
        final Order order = Order.builder()
                .lineItems(new HashSet<>())
                .build();

        final Product product = Product.builder()
                .name("King Burger")
                .description("A burger fit for a king")
                .price(new BigDecimal("12.99"))
                .retired(false)
                .build();

        final Order expectedResponse = Order.builder()
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

        final Order response =
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
        final Order order = Order.builder()
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
        final Order order = Order.builder()
                .lineItems(new HashSet<>())
                .build();
        final Product product = Product.builder()
                .name("King Burger")
                .description("A burger fit for a king")
                .price(new BigDecimal("12.99"))
                .retired(true)
                .build();

    void testFindAllWithNoOrdersReturnsEmptyList() {
        // When
        doReturn(Collections.emptyList())
            .when(orderRepository)
            .findAll();

        List<OrderResponse> response = orderService.findAll();

        // When
        assertTrue(response.isEmpty());
        verify(orderRepository).findAll();
    }

    @Test
    void testUpdateProductQuantity() {
        // Given
        final Product product = Product.builder()
                .id(12L)
                .name("King Burger")
                .description("A burger fit for a king")
                .price(new BigDecimal("12.99"))
                .retired(false)
                .build();

        final Order order = Order.builder()
                .id(12L)
                .address("42 Main Street")
                .build();

        order.setLineItems(Set.of(
                new LineItem(order, product, 1)
        ));

        final Order expectedResult = Order.builder()
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

        final Order actualResult =
                orderService.updateProductQuantity(ORDER_ID, PRODUCT_ID, 2);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(orderRepository).save(any(Order.class));
        verify(orderRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }
//    @Test
//    void testUpdateOrderWithValidOrderIdAndOrderRequestReturnsUpdatedOrder() {
//        // Given
//        Order order = orderTestBuilder.build();
//        OrderRequest orderRequest = orderRequestTestBuilder.build();
//
//        // When
//        doReturn(Optional.of(order))
//            .when(orderRepository)
//            .findById(anyLong());
//        doReturn(order)
//            .when(orderRepository)
//            .save(any(Order.class));
//
//        Order response = orderService.updateOrder(order.getId(), orderRequest);
//
//        // Then
//        assertEquals(OrderStatus.CREATED, response.getOrderStatus());
//        verify(orderRepository).findById(anyLong());
//        verify(orderRepository).save(any(Order.class));
//    }

    @Test
    void testUpdateOrderWithInvalidOrderIdThrowsNotFoundException() {
        // Given
        final Order order = Order.builder()
                .lineItems(new HashSet<>())
                .build();
        OrderRequest orderRequest = orderRequestTestBuilder.withDefaultValues().build();

        // When
        doReturn(Optional.empty())
            .when(orderRepository)
            .findById(anyLong());

        // Then
        assertThrows(NotFoundException.class, () -> orderService.updateOrder(NONEXISTENT_ORDER_ID, orderRequest));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testUpdateOrderWithAlreadyCancelledOrderThrowsOrderCancelledException() {
        // Given
        final Product product = Product.builder()
                .id(12L)
                .name("King Burger")
                .description("A burger fit for a king")
                .price(new BigDecimal("12.99"))
                .retired(false)
                .build();
        final Order order = Order.builder()
                .lineItems(new HashSet<>())
                .build();
        Order order = orderTestBuilder
            .withOrderStatus(OrderStatus.CANCELLED)
            .build();
        OrderRequest orderRequest = orderRequestTestBuilder
            .withDefaultValues().
            build();

        // When
        doReturn(Optional.of(order))
            .when(orderRepository)
            .findById(anyLong());

        // Then
        assertThrows(OrderCancelledException.class, () -> orderService.updateOrder(order.getId(), orderRequest));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testCancelOrderWithValidOrderIdReturnsCancelledOrder() {
        // Given
        final Product product = Product.builder()
                .id(12L)
                .build();
        Order order = orderTestBuilder
            .withDefaultValues()
            .withOrderStatus(OrderStatus.CANCELLED)
            .build();

        final Order order = Order.builder()
                .id(12L)
                .address("42 Main Street")
                .lineItems(new HashSet<>())
                .build();
        final LineItem lineItem = LineItem.builder()
                .order(order)
                .product(product)
                .quantity(5)
                .build();
        order.getLineItems().add(lineItem);

        final Order expectedResponse = Order.builder()
                .id(12L)
                .build();

        // When
        doReturn(Optional.of(order))
            .when(orderRepository)
            .findById(anyLong());
        doReturn(order)
            .when(orderRepository)
            .save(any(Order.class));

        OrderResponse response = orderService.cancelOrder(order.getId());

        // Then
        assertEquals(order.getOrderStatus(), response.getOrderStatus());
        verify(orderRepository).findById(anyLong());
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
        final Order order = Order.builder()
                .lineItems(new HashSet<>())
                .build();

    void testCancelOrderWithInvalidOrderIdThrowsNotFoundException() {
        // When
        doReturn(Optional.empty())
            .when(orderRepository)
            .findById(anyLong());

        // Then
        assertThrows(NotFoundException.class, () -> orderService.cancelOrder(NONEXISTENT_ORDER_ID));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testOrderToOrderResponseMapsSuccessfully() {
        // Given
        final Product product = Product.builder().build();
        final Order order = Order.builder()
                .lineItems(new HashSet<>())
                .build();

        // When
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(productService.findById(anyLong()))
                .thenReturn(product);
        Order order = orderTestBuilder.withDefaultValues().build();
        OrderResponse orderResponse = orderMapper.orderToOrderResponse(order);

        // Then
        assertAll("Should map successfully",
            () -> assertEquals(order.getId(), orderResponse.getId()),
            () -> assertEquals(order.getCreatedOn(), orderResponse.getCreatedOn()),
            () -> assertEquals(order.getLastUpdated(), orderResponse.getLastUpdated()),
            () -> assertEquals(order.getOrderStatus(), orderResponse.getOrderStatus()),
            () -> assertEquals(order.getLineItems(), orderResponse.getLineItems())
        );
    }
}
