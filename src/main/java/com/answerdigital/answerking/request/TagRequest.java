package com.answerdigital.answerking.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record TagRequest(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\\s-]*",
                message = "{tag-request.invalid-name}")
        String name,
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\\s.,!?0-9-']*",
                message = "{tag-request.invalid-description}")
        String description,
        @JsonProperty("products")
        List<Long> productIds
) {}
