package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.LineItem;
import com.answerdigital.answerking.model.Product;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ProductTestBuilder {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean retired;
    private Set<Category> categories;
    private Set<LineItem> lineItems;

    public ProductTestBuilder withDefaultValues() {
        this.id = 1L;
        this.name = "Cheeseburger";
        this.description = "A beef patty with cheddar cheese.";
        this.price = BigDecimal.valueOf(5.00D);
        this.retired = false;
        this.categories = new HashSet<>();
        this.lineItems = new HashSet<>();
        return this;
    }

    public ProductTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ProductTestBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductTestBuilder withRetired(boolean retired) {
        this.retired = retired;
        return this;
    }

    public ProductTestBuilder withCategories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    public ProductTestBuilder withLineItems(Set<LineItem> lineItems) {
        this.lineItems = lineItems;
        return this;
    }

    public Product build() {
        return new Product(id, name, description, price, retired, categories, lineItems);
    }
}
