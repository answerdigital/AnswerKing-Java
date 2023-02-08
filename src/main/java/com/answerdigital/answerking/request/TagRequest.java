package com.answerdigital.answerking.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record TagRequest(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\s-]*",
                message = "Tag name must only contain letters, spaces and dashes")
        String name,
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\s.,!?0-9-']*",
                message = "Tag description can only contain letters, numbers, spaces and !?-.,' punctuation")
        String description,
        @JsonProperty("products")
        List<Long> productIds
) {}
