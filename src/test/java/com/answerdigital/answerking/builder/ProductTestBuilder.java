package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.LineItem;
import com.answerdigital.answerking.model.Product;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ProductTestBuilder {

    private Long id = 1L;
    private String name = "Cheeseburger";
    private String description = "A beef patty with cheddar cheese.";
    private BigDecimal price = BigDecimal.valueOf(5.00D);
    private boolean retired = false;
    private Set<Category> categories = new HashSet<>();
    private Set<LineItem> lineItems = new HashSet<>();
    
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
