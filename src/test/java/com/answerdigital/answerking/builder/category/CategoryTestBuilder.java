package com.answerdigital.answerking.builder.category;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryTestBuilder {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime createdOn;

    private LocalDateTime lastUpdated;

    private boolean retired;

    private List<Product> products;

    public CategoryTestBuilder withDefaultValues() {
        this.id = 1L;
        this.name = "Burgers";
        this.description = "A selection of delicious burgers.";
        this.createdOn = LocalDateTime.of(2022, 1, 1, 0, 0);
        this.lastUpdated = LocalDateTime.of(2022, 1, 1, 0, 0);
        this.retired = false;
        this.products = new ArrayList<>();
        return this;
    }

    public CategoryTestBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public CategoryTestBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CategoryTestBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public CategoryTestBuilder withCreatedOn(final LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public CategoryTestBuilder withLastUpdated(final LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public CategoryTestBuilder withRetired(final boolean retired) {
        this.retired = retired;
        return this;
    }

    public CategoryTestBuilder withProducts(final List<Product> products) {
        this.products = products;
        return this;
    }

    public CategoryTestBuilder withProduct(final Product product) {
        products.add(product);
        return this;
    }

    public Category build() {
        return new Category(id, name, description, createdOn, lastUpdated, retired, products);
    }
}
