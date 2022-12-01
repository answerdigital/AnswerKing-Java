package com.answerdigital.answerking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.PreRemove;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    private boolean retired;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<LineItem> lineItems = new HashSet<>();

    public Product(final String name, final String description, final BigDecimal price, final boolean isRetired) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.retired = isRetired;
    }

    @PreRemove
    private void removeProductsFromCategories() {
        category.removeProduct(this);
    }

    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", retired=" + retired +
                '}';
    }
}
