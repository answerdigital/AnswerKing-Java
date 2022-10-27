package com.answerdigital.benhession.academy.answerkingweek2.model;

import com.answerdigital.benhession.academy.answerkingweek2.request.ItemRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.PreRemove;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "item")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    private Boolean available;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<OrderItem> orderItems;

    public Item() {
    }

    public Item(final ItemRequest itemRequest){
        this.name = itemRequest.name();
        this.description = itemRequest.description();
        this.price = itemRequest.price();
        this.available = itemRequest.available();
    }

    public Item(final String name, final String description, final BigDecimal price, final boolean isAvailable) {
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

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.DOWN);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @JsonIgnore
    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(final Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", available=" + available +
                ", categories=" + categories +
                '}';
    }
}
