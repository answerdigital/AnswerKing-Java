package com.answerdigital.answerking.builder.category;

import com.answerdigital.answerking.response.SimpleCategoryResponse;

public class SimpleCategoryResponseTestBuilder {
    private Long id;

    private String name;

    private String description;

    public SimpleCategoryResponseTestBuilder withDefaultValues() {
        this.id = 1L;
        this.name = "Example Simple Category Response";
        this.description = "This is a simple category response.";
        return this;
    }

    public SimpleCategoryResponseTestBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public SimpleCategoryResponseTestBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public SimpleCategoryResponseTestBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public SimpleCategoryResponse build() {
        return new SimpleCategoryResponse(id, name, description);
    }
}
