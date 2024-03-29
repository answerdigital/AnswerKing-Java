package com.answerdigital.answerking.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

/**
 * The class ProductResponse represents the Product {@link com.answerdigital.answerking.model.Product}
 * to be returned, after the end-user has sent an API endpoint request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Long category;

    private boolean retired;

    @JsonProperty("tags")
    private Set<Long> tagIds;
}
