package com.answerdigital.answerking.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.answerdigital.answerking.util.DateTimeUtility.getDateTimeAsString;

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

    private String createdOn;

    private String lastUpdated;

    private boolean retired;

    @OneToMany(mappedBy="category", fetch = FetchType.EAGER)
    private Set<Product> products = new HashSet<>();

    public Category(final String name, final String description) {
        this.name = name;
        this.description = description;
        this.retired = false;
        this.createdOn = getDateTimeAsString();
        this.lastUpdated = this.createdOn;
    }

    public void addProduct(final Product product) {
        products.add(product);
    }

    public void removeProduct(final Product product) {
        products.remove(product);
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
