package com.answerdigital.benhession.academy.answerkingweek2.model;

import com.answerdigital.benhession.academy.answerkingweek2.dto.AddCategoryDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @OneToMany(mappedBy = "category")
    private final Set<ItemCategory> itemCategories = new HashSet<>();

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Category(AddCategoryDTO addCategoryDTO) {
        this.name = addCategoryDTO.getName();
        this.description = addCategoryDTO.getDescription();
    }

    public Category() {

    }

    void addItemCategory(ItemCategory itemCategory) {
         itemCategories.add(itemCategory);
    }

    void removeItem(Item item) {

        Optional<ItemCategory> itemCategory = itemCategories.stream()
                .filter(ic -> ic.getItem().equals(item))
                .findFirst();

        itemCategory.map(itemCategories::remove);

    }

    public boolean containsItem(Item item) {
        return getItems().contains(item);
    }

    public int getId() {
        return id;
    }

    public Set<Item> getItems() {
        return itemCategories.stream().map(ItemCategory::getItem).collect(Collectors.toSet());
    }

    public Set<ItemCategory> getItemCategories() {
        return new HashSet<>(itemCategories);
    }

    public void clearItemCategories() {
        itemCategories.clear();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", itemCategories=" + itemCategories +
                '}';
    }
}
