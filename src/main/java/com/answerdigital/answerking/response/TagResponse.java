package com.answerdigital.answerking.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {
    private Long id;

    private String name;

    private String description;

    @JsonProperty("products")
    private List<Long> productIds;
}
