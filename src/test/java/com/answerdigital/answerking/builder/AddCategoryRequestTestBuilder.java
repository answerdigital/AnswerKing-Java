package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.request.AddCategoryRequest;

public class AddCategoryRequestTestBuilder {

    private String name = "Burgers";
    private String description = "A selection of delicious burgers.";

    public AddCategoryRequestTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AddCategoryRequestTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public AddCategoryRequest build() {
        return new AddCategoryRequest(name, description);
    }
}
