package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.response.CategoryResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryResponseTestBuilder {
    private Long id;

    private String name;

    private String description;

    private List<Long> productIds;

    private LocalDateTime createdOn;

    private LocalDateTime lastUpdated;

    private boolean retired;

    public CategoryResponseTestBuilder withDefaultValues() {
        this.id = 1L;
        this.name = "Category Response";
        this.description = "This is a Category Response";
        this.productIds = new ArrayList<>();
        this.createdOn = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.retired = false;

        return this;
    }

    public CategoryResponseTestBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public CategoryResponseTestBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CategoryResponseTestBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public CategoryResponseTestBuilder withProductIds(final List<Long> productIds) {
        this.productIds = productIds;
        return this;
    }

    public CategoryResponseTestBuilder withCreatedOn(final LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public CategoryResponseTestBuilder withLastUpdated(final LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public CategoryResponseTestBuilder withIsRetired(final boolean retired) {
        this.retired = retired;
        return this;
    }

    public CategoryResponse build() {
        return new CategoryResponse(id, name, description, productIds, createdOn, lastUpdated, retired);
    }
}
