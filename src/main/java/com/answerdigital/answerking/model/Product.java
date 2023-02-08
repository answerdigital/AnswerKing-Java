package com.answerdigital.answerking.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.PreRemove;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
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

    /**
     * This method removes a Product from a Category {@link com.answerdigital.answerking.model.Category}.
     */
    @PreRemove
    private void removeProductsFromCategories() {
        category.removeProduct(this);
    }

    /**
     * This method returns the price of the Product.
     * @return Price of Product.
     */
    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.DOWN);
    }
}
