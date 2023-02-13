package com.answerdigital.answerking.request;

import jakarta.validation.constraints.Positive;

/**
 * The class LineItemRequest represents the LineItem {@link com.answerdigital.answerking.model.LineItem}
 * received from API endpoint requests.
 */
public record LineItemRequest(
        @Positive(message = "{line-item-request.invalid-id}")
        Long productId,
        @Positive(message = "{line-item-request.invalid-quantity}")
        Integer quantity
) {
}
