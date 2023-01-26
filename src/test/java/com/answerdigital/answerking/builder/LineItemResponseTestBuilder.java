package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.response.LineItemResponse;
import com.answerdigital.answerking.response.ProductResponse;

public class LineItemResponseTestBuilder {
    private ProductResponse product;

    private Integer quantity;

    private final ProductResponseTestBuilder productResponseTestBuilder = new ProductResponseTestBuilder();

    public LineItemResponseTestBuilder withDefaultValues() {
        this.product = productResponseTestBuilder.withDefaultValues().build();
        this.quantity = 1;
        return this;
    }

    public LineItemResponseTestBuilder withProductResponse(final ProductResponse product) {
        this.product = product;
        return this;
    }

    public LineItemResponseTestBuilder withQuantity(final int quantity) {
        this.quantity = quantity;
        return this;
    }

    public LineItemResponse build() {
        return new LineItemResponse(product, quantity);
    }
}
