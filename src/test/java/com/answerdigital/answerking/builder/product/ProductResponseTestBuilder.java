package com.answerdigital.answerking.builder.product;

import com.answerdigital.answerking.response.ProductResponse;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ProductResponseTestBuilder {
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Long category;

    private boolean retired;

    private Set<Long> tags;

    public ProductResponseTestBuilder withDefaultValues() {
        this.id = 1L;
        this.name = "Example Product Response";
        this.description = "This is an example Product Response";
        this.price = new BigDecimal("12.99");
        this.category = 1L;
        this.retired = false;
        this.tags = new HashSet<>();

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

    public ProductResponseTestBuilder withCategory(final Long category) {
        this.category = category;
        return this;
    }

    public ProductResponseTestBuilder withRetired(final boolean retired) {
        this.retired = retired;
        return this;
    }

    public ProductResponseTestBuilder withTags(final Set<Long> tags) {
        this.tags = tags;
        return this;
    }

    public ProductResponse build() {
        return new ProductResponse(id, name, description, price, category, retired, tags);
    }
}
