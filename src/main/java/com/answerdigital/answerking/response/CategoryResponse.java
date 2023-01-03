package com.answerdigital.answerking.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "description", "createdOn", "lastUpdated", "products", "retired"})
public class CategoryResponse {
    private Long id;

    private String name;

    private String description;

    @JsonProperty("products")
    private List<Long> productIds;

    private LocalDateTime createdOn;

    private LocalDateTime lastUpdated;

    private boolean retired;
}
