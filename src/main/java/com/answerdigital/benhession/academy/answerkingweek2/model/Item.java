package com.answerdigital.benhession.academy.answerkingweek2.model;

import com.answerdigital.benhession.academy.answerkingweek2.dto.AddItemDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    @Column(precision = 12, scale = 2)
    @Digits(integer = 12, fraction = 2)
    private BigDecimal price;
    @NotNull
    private Boolean available;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private Set<Category> categories = new HashSet<>();

    public Item(String name, String description, BigDecimal price, boolean isAvailable) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categories = new HashSet<>();
        this.available = isAvailable;
    }

    public Item(AddItemDTO addItemDTO) {
        this.name = addItemDTO.getName();
        this.description = addItemDTO.getDescription();
        this.price = new BigDecimal(addItemDTO.getPrice());
        this.available = addItemDTO.isAvailable();
        this.categories = new HashSet<>();
    }

    public Item() {

    }

    @PreRemove
    private void removeItemsFromCategories() {
        for (Category category : categories) {
            category.getItemsSet().remove(this);
        }
    }
    public Set<Category> getCategories() {
        return categories;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
