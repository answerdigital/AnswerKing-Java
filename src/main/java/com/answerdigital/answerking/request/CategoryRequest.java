package com.answerdigital.answerking.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

/**
 * The class CategoryRequest represents the Category {@link com.answerdigital.answerking.model.Category}
 * received from API endpoint requests.
 */
@JsonPropertyOrder({"name", "description", "products"})
public record CategoryRequest(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\\s-]*",
                message = "{category-request.invalid-name}")
        String name,
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\\s.,!?0-9-']*",
                message = "{category-request.invalid-description}")
        String description,
        @JsonProperty("products")
        List<Long> productIds
) {}
