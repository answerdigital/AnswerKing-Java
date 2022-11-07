package com.answerdigital.academy.answerking.request;

import lombok.Builder;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ItemRequest(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\s-]*",
                message = "Item name must only contain letters, spaces and dashes")
        String name,
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\s.,!?0-9-']*",
                message = "Item description can only contain letters, numbers, spaces and !?-.,' punctuation")
        String description,

        @Digits(integer = 12, fraction = 2, message = "Item price is invalid")
        @DecimalMin(value = "0.0", inclusive = false, message = "Item price is invalid")
        @NotNull
        BigDecimal price,
        @NotNull
        boolean available
) {
    @Builder
    public ItemRequest {
    }
}
