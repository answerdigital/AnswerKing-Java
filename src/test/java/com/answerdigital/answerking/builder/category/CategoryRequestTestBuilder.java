package com.answerdigital.answerking.builder.category;

import com.answerdigital.answerking.request.CategoryRequest;

import java.util.ArrayList;
import java.util.List;

public class CategoryRequestTestBuilder {

    private String name;

    private String description;

    private List<Long> productIds;

    public CategoryRequestTestBuilder withDefaultValues() {
        this.name = "Burgers";
        this.description = "A selection of delicious burgers.";
        this.productIds = new ArrayList<>();
        return this;
    }

    public CategoryRequestTestBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CategoryRequestTestBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public CategoryRequestTestBuilder withProductIds(final List<Long> productIds) {
        this.productIds = productIds;
        return this;
    }

    public CategoryRequest build() {
        return new CategoryRequest(name, description, productIds);
    }
}
