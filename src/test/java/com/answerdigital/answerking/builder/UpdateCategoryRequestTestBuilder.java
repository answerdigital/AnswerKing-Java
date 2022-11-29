package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.request.CategoryRequest;

public class UpdateCategoryRequestTestBuilder {

    private String name;
    private String description;

    public UpdateCategoryRequestTestBuilder withDefaultValues() {
        this.name = "Pizzas";
        this.description = "Italian style stone baked pizzas.";
        return this;
    }

    public UpdateCategoryRequestTestBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public UpdateCategoryRequestTestBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public CategoryRequest build() {
        return new CategoryRequest(name, description);
    }
}
