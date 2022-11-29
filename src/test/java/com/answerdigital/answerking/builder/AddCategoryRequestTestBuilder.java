package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.request.AddCategoryRequest;

public class AddCategoryRequestTestBuilder {

    private String name;

    private String description;

    public AddCategoryRequestTestBuilder withDefaultValues() {
        this.name = "Burgers";
        this.description = "A selection of delicious burgers.";
        return this;
    }

    public AddCategoryRequestTestBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public AddCategoryRequestTestBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public AddCategoryRequest build() {
        return new AddCategoryRequest(name, description);
    }
}
