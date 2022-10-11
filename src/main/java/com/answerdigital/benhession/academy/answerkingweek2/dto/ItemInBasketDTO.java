package com.answerdigital.benhession.academy.answerkingweek2.dto;

import com.answerdigital.benhession.academy.answerkingweek2.model.Item;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemInBasketDTO {

    private final int id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final boolean available;
    private final Set<CategoryNoItemsDTO> categories;
    private final int quantity;

    public ItemInBasketDTO(Item item, Integer quantity) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.available = item.isAvailable();
        this.categories = item.getCategories().stream()
                .map(CategoryNoItemsDTO::new)
                .collect(Collectors.toSet());
        this.quantity = quantity;
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

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public Set<CategoryNoItemsDTO> getCategories() {
        return categories;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemInBasketDTO that = (ItemInBasketDTO) o;
        return id == that.id && available == that.available && quantity == that.quantity && name.equals(that.name) && description.equals(that.description) && price.equals(that.price) && categories.equals(that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, available, categories, quantity);
    }

    @Override
    public String toString() {
        return "ItemInBasketDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", available=" + available +
                ", categories=" + categories +
                ", quantity=" + quantity +
                '}';
    }
}
