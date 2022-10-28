package com.answerdigital.benhession.academy.answerkingweek2.model;

import com.answerdigital.benhession.academy.answerkingweek2.request.ItemRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "item")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
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
    private Set<OrderItem> orderItems = new HashSet<>();

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public void setAvailable(final Boolean available) {
        this.available = available;
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
            category.getItems().remove(this);
        }
    }

    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Item item = (Item) o;
        return Objects.equals(id, item.id);
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
                '}';
    }
}
