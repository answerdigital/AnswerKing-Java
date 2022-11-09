package com.answerdigital.academy.answerking.model;

import com.answerdigital.academy.answerking.request.AddCategoryRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\s-]*", message = "Category name must only contain letters, spaces and dashes")
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\s.,!?0-9-']*",
            message = "Category description can only contain letters, numbers, spaces and !?-.,' punctuation")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "item_category",
            joinColumns = {@JoinColumn(name = "category_id")},
            inverseJoinColumns = {@JoinColumn(name = "item_id")})
    @JsonIgnore
    private Set<Item> items = new HashSet<>();

    public Category(final AddCategoryRequest categoryRequest) {
        this.name = categoryRequest.name();
        this.description = categoryRequest.description();
    }

    public void addItem(final Item item) {
        items.add(item);
    }

    public void removeItem(final Item item) {
        items.remove(item);
    }

    public Category(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Category category = (Category) o;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
