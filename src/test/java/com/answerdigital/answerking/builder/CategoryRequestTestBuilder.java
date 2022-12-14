package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.request.CategoryRequest;

public class CategoryRequestTestBuilder {

    private String name;

    private String description;

    public CategoryRequestTestBuilder withDefaultAddRequestValues() {
        this.name = "Burgers";
        this.description = "A selection of delicious burgers.";
        return this;
    }

    public CategoryRequestTestBuilder withDefaultUpdateRequestValues() {
        this.name = "Pizzas";
        this.description = "Italian style stone baked pizzas.";
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

    public CategoryRequest build() {
        return new CategoryRequest(name, description);
    }
}
