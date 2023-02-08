package com.answerdigital.answerking.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The class SimpleCategoryResponse represents the Category {@link com.answerdigital.answerking.model.Category}
 * to be returned, after the end-user has sent an API endpoint request.
 * This class returns a simpler response in comparison to {@link com.answerdigital.answerking.response.CategoryResponse}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "description"})
public class SimpleCategoryResponse {
    private Long id;

    private String name;

    private String description;
}
