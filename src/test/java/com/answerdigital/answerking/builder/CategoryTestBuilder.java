package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;

import java.util.HashSet;
import java.util.Set;

import static com.answerdigital.answerking.util.DateTimeUtility.getDateTimeAsString;

public class CategoryTestBuilder {

    private Long id = 1L;
    private String name = "Burgers";
    private String description = "A selection of delicious burgers.";
    private String createdOn = getDateTimeAsString();
    private String lastUpdated = getDateTimeAsString();;
    private boolean retired = false;
    private Set<Product> products = new HashSet<>();

    public CategoryTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CategoryTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CategoryTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CategoryTestBuilder withCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public CategoryTestBuilder withLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public CategoryTestBuilder withRetired(boolean retired) {
        this.retired = retired;
        return this;
    }

    public CategoryTestBuilder withProducts(Set<Product> products) {
        this.products = products;
        return this;
    }

    // creates a HashSet of one product, to remove boilerplate
    public CategoryTestBuilder withProduct(Product product) {
        Set<Product> products = new HashSet<>();
        products.add(product);
        this.products = products;
        return this;
    }

    public Category build() {
        return new Category(id, name, description, createdOn, lastUpdated, retired, products);
    }
}
