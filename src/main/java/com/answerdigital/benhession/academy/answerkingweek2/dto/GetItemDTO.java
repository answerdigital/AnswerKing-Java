package com.answerdigital.benhession.academy.answerkingweek2.dto;

import com.answerdigital.benhession.academy.answerkingweek2.model.Item;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GetItemDTO {

    private final int id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final boolean available;
    private final Set<CategoryNoItemsDTO> categories;

    public GetItemDTO(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.available = item.isAvailable();
        this.categories = item.getCategories().stream()
                .map(CategoryNoItemsDTO::new)
                .collect(Collectors.toSet());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetItemDTO that = (GetItemDTO) o;
        return id == that.id && available == that.available && name.equals(that.name) && description.equals(that.description) && price.equals(that.price) && categories.equals(that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, available, categories);
    }

    @Override
    public String toString() {
        return "GetItemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", available=" + available +
                ", categories=" + categories +
                '}';
    }
}
