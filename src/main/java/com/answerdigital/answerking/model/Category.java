package com.answerdigital.answerking.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    private boolean retired;

    @OneToMany(mappedBy="category", fetch = FetchType.EAGER)
    private Set<Product> products = new HashSet<>();

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
}
