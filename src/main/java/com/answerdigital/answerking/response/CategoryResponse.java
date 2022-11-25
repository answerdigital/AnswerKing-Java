package com.answerdigital.answerking.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\s-]*",
            message = "Category name must only contain letters, spaces and dashes")
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\s.,!?0-9-']*",
            message = "Category description can only contain letters, numbers, spaces and !?-.,' punctuation")
    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Long> productIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdOn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String lastUpdated;

    private boolean retired;

}
