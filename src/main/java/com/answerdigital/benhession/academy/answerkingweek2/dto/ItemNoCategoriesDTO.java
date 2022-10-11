package com.answerdigital.benhession.academy.answerkingweek2.dto;

import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemNoCategoriesDTO {
    private final int id;
    private final String name;
    private final String description;
    private final boolean available;
    private final BigDecimal price;

    public ItemNoCategoriesDTO(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.available = item.isAvailable();
        this.price = item.getPrice();
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

    public boolean isAvailable() {
        return available;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemNoCategoriesDTO that = (ItemNoCategoriesDTO) o;
        return id == that.id && available == that.available && name.equals(that.name) && description.equals(that.description) && price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, price);
    }

    @Override
    public String toString() {
        return "ItemNoCategoriesDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", price=" + price +
                '}';
    }
}
