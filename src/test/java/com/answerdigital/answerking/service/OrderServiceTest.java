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
//
//    @Test
//    void testFindByIdWithValidIdReturnsFoundOrder() {
//        // Given
//        Order order = orderTestBuilder.withDefaultValues().build();
//
//        // When
//        doReturn(Optional.of(order))
//            .when(orderRepository)
//            .findById(anyLong());
//
//        OrderResponse response = orderService.findById(order.getId());
//
//        // Then
//        assertEquals(response, order);
//        verify(orderRepository).findById(anyLong());
//    }

    @Test
    void testFindByIdWithInvalidIdThrowsNotFoundException() {
        // When
        doReturn(Optional.empty())
            .when(orderRepository)
            .findById(anyLong());

        // Then
        assertThrows(NotFoundException.class, () -> orderService.findById(NONEXISTENT_ORDER_ID));
        verify(orderRepository).findById(anyLong());
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
        Order order = orderTestBuilder
            .withDefaultValues()
            .withOrderStatus(OrderStatus.CANCELLED)
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
        Order order = orderTestBuilder.withDefaultValues().build();
        OrderResponse orderResponse = orderMapper.orderToOrderResponse(order);
        Order expected = orderMapper.orderResponseToOrder(orderResponse);

        // Then
        assertAll("Should map successfully",
            () -> assertEquals(expected.getId(), orderResponse.getId()),
            () -> assertEquals(expected.getCreatedOn(), orderResponse.getCreatedOn()),
            () -> assertEquals(expected.getLastUpdated(), orderResponse.getLastUpdated()),
            () -> assertEquals(expected.getOrderStatus(), orderResponse.getOrderStatus()),
            () -> assertEquals(expected.getLineItems(), orderResponse.getLineItems())
        );
    }
}
