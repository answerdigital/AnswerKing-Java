package com.answerdigital.answerking.request;

import jakarta.validation.constraints.Positive;

/**
 * The class LineItemRequest represents the LineItem {@link com.answerdigital.answerking.model.LineItem}
 * received from API endpoint requests.
 */
public record LineItemRequest(
        @Positive(message = "The product ID must be a positive number")
        Long productId,
        @Positive(message = "The quantity must be a positive number")
        Integer quantity
) {
}
