package com.answerdigital.benhession.academy.answerkingweek2.dto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class AddCategoryDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    public AddCategoryDTO() {

    }

    public AddCategoryDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public boolean isNotValid() {
        return !name.matches("^[a-zA-Z\s-]*") ||
                !description.matches("^[a-zA-Z\s.,!?0-9-']*");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddCategoryDTO that = (AddCategoryDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
