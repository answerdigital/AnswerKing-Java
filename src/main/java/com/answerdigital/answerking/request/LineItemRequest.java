package com.answerdigital.answerking.request;

import javax.validation.constraints.Positive;

public record LineItemRequest(
        @Positive(message = "The product ID must be a positive number")
        Long productId,
        @Positive(message = "The quantity must be a positive number")
        Integer quantity
) { }
