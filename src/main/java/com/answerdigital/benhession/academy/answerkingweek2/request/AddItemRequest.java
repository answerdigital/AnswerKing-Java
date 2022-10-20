package com.answerdigital.benhession.academy.answerkingweek2.request;

import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public record AddItemRequest(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\s-]*",
                message = "Category name must only contain letters, spaces and dashes")
        String name,
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\s.,!?0-9-']*",
                message = "Item description can only contain letters, numbers, spaces and !?-.,' punctuation")
        String description,

        @Digits(integer = 12, fraction = 2, message = "Item price is invalid")
        @NotBlank
        BigDecimal price,
        @NotBlank
        boolean available
){}
