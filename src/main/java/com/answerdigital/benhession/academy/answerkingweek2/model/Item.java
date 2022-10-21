package com.answerdigital.benhession.academy.answerkingweek2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.answerdigital.benhession.academy.answerkingweek2.request.ItemRequest;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\s-]*", message = "Item name must only contain letters, spaces and dashes")
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\s.,!?0-9-']*",
            message = "Item description can only contain letters, numbers, spaces and !?-.,' punctuation")
    private String description;

    @Column(precision = 12, scale = 2)
    @Digits(integer = 12, fraction = 2)
    @NotBlank
    @Pattern(regexp = "^[0-9]*.[0-9]{2}", message = "Item price is in invalid format")
    private BigDecimal price;

    @NotNull
    private Boolean available;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private Set<Category> categories = new HashSet<>();

    public Item() {
    }

    public Item(ItemRequest itemRequest){
        this.name = itemRequest.name();
        this.description = itemRequest.description();
        this.price = itemRequest.price();
        this.available = itemRequest.available();
    }

    public Item(String name, String description, BigDecimal price, boolean isAvailable) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categories = new HashSet<>();
        this.available = isAvailable;
    }

    @PreRemove
    private void removeItemsFromCategories() {
        for (Category category : categories) {
            category.getItemsSet().remove(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
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
