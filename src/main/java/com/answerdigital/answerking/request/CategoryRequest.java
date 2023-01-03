package com.answerdigital.answerking.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonPropertyOrder({"name", "description", "products"})
public record CategoryRequest(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\s-]*",
                message = "Category name must only contain letters, spaces and dashes")
        String name,
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\s.,!?0-9-']*",
                message = "Category description can only contain letters, numbers, spaces and !?-.,' punctuation")
        String description,
        @JsonProperty("products")
        List<Long> productIds
) {}
