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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String name;

    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Long> productIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdOn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String lastUpdated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean retired;

}
