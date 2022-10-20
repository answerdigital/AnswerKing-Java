package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CategoryService {

    private final ItemService itemService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(ItemService itemService, CategoryRepository categoryRepository) {
        this.itemService = itemService;
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new ConflictException(String.format("A category named '%s' already exists", category.getName()));
        }
        return categoryRepository.save(category);
    }

    private Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with ID %d does not exist.", categoryId)));
    }

    public Set<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(Category categoryUpdate) {
        // check that the category isn't being renamed to a category name that already exists
        if (categoryRepository.existsByNameAndIdIsNot(categoryUpdate.getName(), categoryUpdate.getId())) {
            throw new ConflictException(String.format("A category named %s already exists", categoryUpdate.getName()));
        }

        Category existingCategory = findById(categoryUpdate.getId());

        // checks if new category details supplied does not have item list but
        // existing category has an item list it will keep existing item list in the update
        if ((categoryUpdate.getItemsSet() == null || categoryUpdate.getItemsSet().isEmpty())
                && !existingCategory.getItemsSet().isEmpty()) {
            categoryUpdate.getItemsSet().addAll(existingCategory.getItemsSet());
        }

        return categoryRepository.save(categoryUpdate);
    }

    public Category addItemToCategory(Long categoryId, Long itemId) {
        Category category = findById(categoryId);
        Item item = itemService.findById(itemId);

        category.getItemsSet().add(item);
        return categoryRepository.save(category);
    }

    public Category removeItemFromCategory(Long categoryId, Long itemId) {
        Category category = findById(categoryId);
        Item item = itemService.findById(itemId);

        category.getItemsSet().remove(item);
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(Long categoryId) {
        findById(categoryId);
        categoryRepository.deleteById(categoryId);
    }
}