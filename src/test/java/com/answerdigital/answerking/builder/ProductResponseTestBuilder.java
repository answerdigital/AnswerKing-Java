package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.response.ProductResponse;
import com.answerdigital.answerking.response.SimpleCategoryResponse;
import java.math.BigDecimal;

public class ProductResponseTestBuilder {
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private SimpleCategoryResponse category;

    private boolean retired;

    public ProductResponseTestBuilder withDefaultValues() {
        this.id = 1L;
        this.name = "Example Product Response";
        this.description = "This is an example Product Response";
        this.price = new BigDecimal("12.99");
        this.category = new SimpleCategoryResponse();
        this.retired = false;

        return this;
    }

    public ProductResponseTestBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public ProductResponseTestBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public ProductResponseTestBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public ProductResponseTestBuilder withPrice(final BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductResponseTestBuilder withCategory(final SimpleCategoryResponse category) {
        this.category = category;
        return this;
    }

    public ProductResponseTestBuilder withRetired(final boolean retired) {
        this.retired = retired;
        return this;
    }

    public ProductResponse build() {
        return new ProductResponse(id, name, description, price, category, retired);
    }
}
