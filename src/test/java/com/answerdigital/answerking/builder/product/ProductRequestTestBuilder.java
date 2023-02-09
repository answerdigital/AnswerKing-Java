package com.answerdigital.answerking.builder.product;

import com.answerdigital.answerking.request.ProductRequest;

import java.math.BigDecimal;

public class ProductRequestTestBuilder {

    private String name;

    private String description;

    private BigDecimal price;

    private Long categoryId;

    public ProductRequestTestBuilder withDefaultValues() {
        this.name = "Cheeseburger";
        this.description = "A beef patty with cheddar cheese.";
        this.price = BigDecimal.valueOf(5.00D);
        this.categoryId = 1L;
        return this;
    }

    public ProductRequestTestBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public ProductRequestTestBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public ProductRequestTestBuilder withPrice(final BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductRequest build() {
        return new ProductRequest(name, description, price, categoryId);
    }
}
