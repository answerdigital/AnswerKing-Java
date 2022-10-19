package com.answerdigital.benhession.academy.answerkingweek2.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;
import java.util.Set;

public class AddItemDTO {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\s-]*", message = "Item name must only contain letters and dashes")
    private String name;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\s.,!?0-9-']*",
            message = "Item description can only contain letters, numbers, spaces and !?-.,' punctuation")
    private String description;
    @NotBlank
    @Pattern(regexp = "^[0-9]*.[0-9]{2}", message = "Item price is in invalid format")
    private String price;
    @NotNull
    private Boolean available;
    @NotNull
    private Set<Integer> categoryIds;

    public AddItemDTO(){
    }

    public AddItemDTO(String name, String description, String price, Boolean available, Set<Integer> categoryIds) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.price = price;
        this.categoryIds = categoryIds;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public Set<Integer> getCategoryIds() {
        return categoryIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddItemDTO that = (AddItemDTO) o;
        return name.equals(that.name) && description.equals(that.description) && price.equals(that.price) && available.equals(that.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, available);
    }

    @Override
    public String toString() {
        return "AddItemDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", isAvailable=" + available +
                '}';
    }
}
