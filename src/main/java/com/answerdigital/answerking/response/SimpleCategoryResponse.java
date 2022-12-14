package com.answerdigital.answerking.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCategoryResponse {

    private Long id;

    private String name;

    private String description;
}
