package com.answerdigital.answerking.response;

import com.answerdigital.answerking.builder.LineItemResponseTestBuilder;
import com.answerdigital.answerking.builder.ProductResponseTestBuilder;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OrderResponseTest {
    private final ProductResponseTestBuilder productResponseTestBuilder = new ProductResponseTestBuilder();

    private final LineItemResponseTestBuilder lineItemResponseTestBuilder = new LineItemResponseTestBuilder();

    @Test
    public void testOrderTotalWhenBasketIsEmptyIsZero() {
        final OrderResponse orderResponse = new OrderResponse();
        orderResponse.setLineItems(new HashSet<>());
        assertEquals("0.00", orderResponse.getOrderTotal().toString());
    }

    @Test
    public void testGetOrderTotalSuccessfully() {
        final ProductResponse product = productResponseTestBuilder.withDefaultValues().build();
        final LineItemResponse lineItemResponse = lineItemResponseTestBuilder
                .withProductResponse(product)
                .withQuantity(1)
                .build();
        final LineItemResponse lineItemResponseTwo = lineItemResponseTestBuilder
                .withProductResponse(product)
                .withQuantity(10)
                .build();

        final OrderResponse orderResponse = new OrderResponse();
        orderResponse.setLineItems(Set.of(lineItemResponse, lineItemResponseTwo));
        assertEquals("142.89", orderResponse.getOrderTotal().toString());
        assertEquals("12.99", orderResponse.getLineItems()
                .stream()
                .filter(lineItemResponse1 -> lineItemResponse1.getQuantity() == 1)
                .findFirst()
                .get().getSubTotal().toString());
    }
}

