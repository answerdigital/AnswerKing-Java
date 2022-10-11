package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.UnableToSaveEntityException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.model.ItemCategory;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.CategoryRepository;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.ItemCategoryRepository;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    CategoryRepository categoryRepository;
    ItemRepository itemRepository;
    ItemCategoryRepository itemCategoryRepository;
    Logger logger = LoggerFactory.getLogger("Category service");

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ItemRepository itemRepository,
                           ItemCategoryRepository itemCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
        this.itemCategoryRepository = itemCategoryRepository;
    }

    public Optional<Category> addCategory(Category category) throws UnableToSaveEntityException {
        if (categoryRepository.existsByName(category.getName())) {
            return Optional.empty();
        } else {
            return Optional.of(save(category));
        }
    }

    public Optional<Category> updateCategory(Category category) throws UnableToSaveEntityException {

        boolean hasNameConflict = categoryRepository.existsByNameAndIdIsNot(category.getName(), category.getId());

        if (hasNameConflict) {
            return Optional.empty();
        } else {
            return Optional.of(save(category));
        }

    }

    private Category save(Category category) throws UnableToSaveEntityException {
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            logger.error("save category: save operation failed");
            throw new UnableToSaveEntityException(String.format("Unable to save category = %s", category.getName()));
        }
    }

    public Optional<Set<Category>> getAll() {
        Set<Category> currentCategories = categoryRepository.findAll();

        return currentCategories.isEmpty() ? Optional.empty() : Optional.of(currentCategories);
    }

    public Optional<Category> findById(int categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public Category remove(Category category) {
        Set<Item> items = category.getItems();
        Set<ItemCategory> itemCategories = category.getItemCategories();
        items.forEach(item -> item.remove(category));
        category.clearItemCategories();

        itemCategoryRepository.deleteAll(itemCategories);
        itemRepository.saveAll(items);
        categoryRepository.delete(category);

        return category;
    }

    public boolean allExist(Set<Integer> ids) {
        return ids.size() == categoryRepository.countByIdIn(ids);
    }
}
