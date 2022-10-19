package com.answerdigital.benhession.academy.answerkingweek2.dto;

import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import com.answerdigital.benhession.academy.answerkingweek2.model.ItemCategory;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GetCategoryDTO {
    private final int id;
    private final String name;
    private final String description;
    private final Set<ItemCategory> items;

    public GetCategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.items = category.getItems().stream()
                .map(ItemNoCategoriesDTO::new)
                .collect(Collectors.toSet());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<ItemNoCategoriesDTO> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetCategoryDTO that = (GetCategoryDTO) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, items);
    }

    @Override
    public String toString() {
        return "GetCategoriesDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", items=" + items +
                '}';
    }
}
