package com.answerdigital.answerking.request;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public record LineItemRequest(
        @Positive(message = "The product ID must be a positive number")
        Long productId,
        @PositiveOrZero(message = "The quantity must be must be greater than or equal to 0")
        Integer quantity
) { }