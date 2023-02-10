package com.answerdigital.answerking.builder.product;

import com.answerdigital.answerking.response.SimpleProductResponse;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class SimpleProductResponseTestBuilder {
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private boolean retired;

    private Set<Long> tags;

    public SimpleProductResponseTestBuilder withDefaultValues() {
        this.id = 1L;
        this.name = "Example Product Response";
        this.description = "This is an example Product Response";
        this.price = new BigDecimal("12.99");
        this.retired = false;
        this.tags = new HashSet<>();

        return this;
    }

    public SimpleProductResponseTestBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public SimpleProductResponseTestBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public SimpleProductResponseTestBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public SimpleProductResponseTestBuilder withPrice(final BigDecimal price) {
        this.price = price;
        return this;
    }

    public SimpleProductResponseTestBuilder withRetired(final boolean retired) {
        this.retired = retired;
        return this;
    }

    public SimpleProductResponseTestBuilder withTags(final Set<Long> tags) {
        this.tags = tags;
        return this;
    }

    public SimpleProductResponse build() {
        return new SimpleProductResponse(id, name, description, price, retired, tags);
    }
}
