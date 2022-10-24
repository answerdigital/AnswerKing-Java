package com.answerdigital.benhession.academy.answerkingweek2.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record UpdateCategoryRequest(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\s-]*", message = "Category name must only contain letters, spaces and dashes")
        String name,
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\s.,!?0-9-']*",
        message = "Category description can only contain letters, numbers, spaces and !?-.,' punctuation")
        String description
) {}