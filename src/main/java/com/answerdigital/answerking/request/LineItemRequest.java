package com.answerdigital.answerking.request;

import javax.validation.constraints.Positive;
import java.util.Objects;

public record LineItemRequest(
        @Positive(message = "The product ID must be a positive number")
        Long productId,
        @Positive(message = "The quantity must be a positive number")
        Integer quantity
) {
        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                LineItemRequest that = (LineItemRequest) o;
                return productId.equals(that.productId);
        }

        @Override
        public int hashCode() {
                return Objects.hash(productId);
        }
}
