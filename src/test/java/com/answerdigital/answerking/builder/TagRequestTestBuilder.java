package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.request.TagRequest;

import java.util.ArrayList;
import java.util.List;

public class TagRequestTestBuilder {

    private String name;

    private String description;

    private List<Long> productIds;

    public TagRequestTestBuilder withDefaultValues() {
        this.name = "Gluten Free";
        this.description = "This does not contain Gluten.";
        this.productIds = new ArrayList<>();
        return this;
    }

    public TagRequestTestBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public TagRequestTestBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public TagRequestTestBuilder withProductIds(final List<Long> productIds) {
        this.productIds = productIds;
        return this;
    }

    public TagRequestTestBuilder withProductId(final Long productId) {
        productIds.add(productId);
        return this;
    }

    public TagRequest build() {
        return new TagRequest(name, description, productIds);
    }
}
