package com.answerdigital.answerking.request;

import lombok.Builder;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\s-]*",
                message = "Product name must only contain letters, spaces and dashes")
        String name,
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\s.,!?0-9-']*",
                message = "Product description can only contain letters, numbers, spaces and !?-.,' punctuation")
        String description,

        @Digits(integer = 12, fraction = 2, message = "Product price is invalid")
        @DecimalMin(value = "0.0", inclusive = false, message = "Product price cannot be less than 0")
        @NotNull
        BigDecimal price,
        @NotNull
        Long categoryId
) {
    @Builder
    public ProductRequest {
    }
}
