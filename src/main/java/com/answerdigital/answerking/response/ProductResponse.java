package com.answerdigital.answerking.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    @NotBlank
    private Long id;

    @Pattern(regexp = "^[a-zA-Z\s-]*",
            message = "Product name must only contain letters, spaces and dashes")
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\s.,!?0-9-']*",
            message = "Product description can only contain letters, numbers, spaces and !?-.,' punctuation")
    private String description;

    @Digits(integer = 12, fraction = 2, message = "Product price is invalid")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product price cannot be less than 0")
    @NotNull
    private BigDecimal price;

    private List<Long> categories;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean retired;

}
