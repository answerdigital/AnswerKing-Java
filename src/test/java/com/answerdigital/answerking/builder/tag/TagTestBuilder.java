package com.answerdigital.answerking.builder.tag;

import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagTestBuilder {

    private Long id;

    private String name;

    private String description;

    private List<Product> products;

    private boolean retired;

    public TagTestBuilder withDefaultValues() {
        this.id = 1L;
        this.name = "Vegan";
        this.description = "This is suitable for Vegans.";
        this.products = new ArrayList<>();
        this.retired = false;
        return this;
    }

    public TagTestBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public TagTestBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public TagTestBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public TagTestBuilder withProducts(final List<Product> products) {
        this.products = products;
        return this;
    }

    public TagTestBuilder withProduct(final Product product) {
        products.add(product);
        return this;
    }

    public TagTestBuilder withRetired(final boolean retired) {
        this.retired = retired;
        return this;
    }

    public Tag build() {
        return new Tag(id, name, description, retired, products);
    }
}
