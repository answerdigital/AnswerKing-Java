package com.answerdigital.answerking.service;

import com.answerdigital.answerking.builder.OrderRequestTestBuilder;
import com.answerdigital.answerking.builder.OrderTestBuilder;
import com.answerdigital.answerking.exception.custom.OrderCancelledException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.mapper.OrderMapper;
import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.model.OrderStatus;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.repository.OrderRepository;
import com.answerdigital.answerking.request.LineItemRequest;
import com.answerdigital.answerking.request.OrderRequest;
import com.answerdigital.answerking.response.OrderResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
        final Order order = orderTestBuilder
            .withDefaultValues()
            .build();
        final OrderRequest orderRequest = orderRequestTestBuilder
            .withLineItemRequests(new ArrayList<>())
            .build();

        // When
        doReturn(order)
            .when(orderRepository)
            .save(any(Order.class));

        final OrderResponse response = orderService.addOrder(orderRequest);

        // Then
        assertEquals(OrderStatus.CREATED, response.getOrderStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testGetOrderResponseByIdReturnsFoundOrder() {
        // given
        final Order order = orderTestBuilder
            .withDefaultValues()
            .build();

        // when
        doReturn(Optional.of(order))
            .when(orderRepository)
            .findById(anyLong());

        final OrderResponse response = orderService.getOrderResponseById(order.getId());

        // then
        assertAll("Should be equal",
            () -> assertEquals(order.getId(), response.getId()),
            () -> assertEquals(order.getLastUpdated(), response.getLastUpdated()),
            () -> assertEquals(order.getCreatedOn(), response.getCreatedOn()),
            () -> assertEquals(order.getOrderStatus(), response.getOrderStatus())
        );
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testGetOrderResponseByIdWithInvalidIdThrowsNotFoundException() {
        // when
        doReturn(Optional.empty())
            .when(orderRepository)
            .findById(anyLong());

        // then
        assertThrows(NotFoundException.class, () -> orderService.getOrderResponseById(NONEXISTENT_ORDER_ID));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testFindAllReturnsEmptyListOfOrders() {
        // given
        final List<Order> orders = List.of();

        // when
        doReturn(orders)
            .when(orderRepository)
            .findAll();

        final List<OrderResponse> response = orderService.findAll();

        // then
        assertTrue(response.isEmpty());
        verify(orderRepository).findAll();
    }

    @Test
    void testFindAllReturnsListOfOrders() {
        // given
        final List<Order> orders = List.of(
            orderTestBuilder.withId(1L).build(),
            orderTestBuilder.withId(2L).build()
        );

        // when
        doReturn(orders)
            .when(orderRepository)
            .findAll();

        final List<OrderResponse> response = orderService.findAll();

        // then
        assertEquals(2, response.size());
        assertFalse(response.isEmpty());
        verify(orderRepository).findAll();
    }

    @Test
    void testUpdateOrder() {
        // given
        final Order order = orderTestBuilder
            .withDefaultValues()
            .build();

        final OrderRequest updateOrderRequest = orderRequestTestBuilder
                .withLineItemRequests(List.of(new LineItemRequest(1L, 1)))
                .build();

        final Product product = Product.builder()
                .id(1L)
                .name("burger")
                .build();

        final Order expectedOrder = new Order();

        // when
        doReturn(Optional.of(order))
            .when(orderRepository)
            .findById(anyLong());
        doReturn(expectedOrder)
            .when(orderRepository)
            .save(any(Order.class));
        doReturn(List.of(product))
            .when(productService)
            .findAllProductsInListOfIds(anyList());

        final OrderResponse response = orderService.updateOrder(order.getId(), updateOrderRequest);

        // then
        assertEquals(expectedOrder.getLineItems().isEmpty(), response.getLineItems().isEmpty());
        verify(orderRepository).findById(anyLong());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testUpdateOrderWhenOrderDoesNotExistThrowsNotFoundException() {
        // given
        final OrderRequest orderRequest = orderRequestTestBuilder
            .withDefaultValues()
            .build();

        // when
        doReturn(Optional.empty())
            .when(orderRepository)
            .findById(anyLong());

        // then
        assertThrows(NotFoundException.class, () ->
                orderService.updateOrder(NONEXISTENT_ORDER_ID, orderRequest));
    }

    @Test
    void testFindAllWithNoOrdersReturnsEmptyList() {
        // When
        doReturn(Collections.emptyList())
            .when(orderRepository)
            .findAll();

        final List<OrderResponse> response = orderService.findAll();

        // When
        assertTrue(response.isEmpty());
        verify(orderRepository).findAll();
    }

    @Test
    void testUpdateOrderWithInvalidOrderIdThrowsNotFoundException() {
        // Given
        final OrderRequest orderRequest = orderRequestTestBuilder.withDefaultValues().build();

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
        final Order order = orderTestBuilder
            .withDefaultValues()
            .withId(1L)
            .withOrderStatus(OrderStatus.CANCELLED)
            .build();
        final OrderRequest updateOrderRequest = orderRequestTestBuilder
            .withLineItemRequests(List.of(new LineItemRequest(1L, 1)))
            .build();

        // When
        doReturn(Optional.of(order))
            .when(orderRepository)
            .findById(anyLong());

        // Then
        assertThrows(OrderCancelledException.class, () -> orderService.updateOrder(1L, updateOrderRequest));
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testOrderToOrderResponseMapsSuccessfully() {
        // Given
        final Order order = orderTestBuilder.withDefaultValues().build();

        // When
        final OrderResponse orderResponse = orderMapper.orderToOrderResponse(order);

        System.out.println(orderResponse);
        System.out.println(order);
        // Then
        assertAll("Should map successfully",
            () -> assertEquals(order.getId(), orderResponse.getId()),
            () -> assertEquals(order.getCreatedOn(), orderResponse.getCreatedOn()),
            () -> assertEquals(order.getLastUpdated(), orderResponse.getLastUpdated()),
            () -> assertEquals(order.getOrderStatus(), orderResponse.getOrderStatus())
        );
    }
}
