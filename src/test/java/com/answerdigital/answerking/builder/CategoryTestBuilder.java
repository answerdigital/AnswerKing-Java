package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;

import java.util.HashSet;
import java.util.Set;

import static com.answerdigital.answerking.util.DateTimeUtility.getDateTimeAsString;

public class CategoryTestBuilder {

    private Long id = 1L;

    private String name;

    private String description;

    private String createdOn;

    private String lastUpdated;

    private boolean retired;

    private Set<Product> products;

    public CategoryTestBuilder withDefaultValues() {
        this.id = 1L;
        this.name = "Burgers";
        this.description = "A selection of delicious burgers.";
        this.createdOn = getDateTimeAsString();
        this.lastUpdated = getDateTimeAsString();
        this.retired = false;
        this.products = new HashSet<>();
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

    public CategoryTestBuilder withCreatedOn(final String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public CategoryTestBuilder withLastUpdated(final String lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public CategoryTestBuilder withRetired(final boolean retired) {
        this.retired = retired;
        return this;
    }

    public CategoryTestBuilder withProducts(final Set<Product> products) {
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
