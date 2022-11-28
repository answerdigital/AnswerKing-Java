package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.request.UpdateCategoryRequest;

public class UpdateCategoryRequestTestBuilder {

    private String name = "Pizzas";
    private String description = "Italian style stone baked pizzas.";

    public UpdateCategoryRequestTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UpdateCategoryRequestTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public UpdateCategoryRequest build() {
        return new UpdateCategoryRequest(name, description);
    }
}
