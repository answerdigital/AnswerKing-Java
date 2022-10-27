package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.CategoryRepository;
import com.answerdigital.benhession.academy.answerkingweek2.request.AddCategoryRequest;
import com.answerdigital.benhession.academy.answerkingweek2.request.UpdateCategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CategoryService {
    private final ItemService itemService;

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(final ItemService itemService, final CategoryRepository categoryRepository) {
        this.itemService = itemService;
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(final AddCategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.name())) {
            throw new ConflictException(String.format("A category named '%s' already exists", categoryRequest.name()));
        }

        final Category category = new Category(categoryRequest);
        return categoryRepository.save(category);
    }

    private Category findById(final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with ID %d does not exist.", categoryId)));
    }

    public Set<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(final UpdateCategoryRequest updateCategoryRequest, final Long id) {
        // check that the category isn't being renamed to a category name that already exists
        if (categoryRepository.existsByNameAndIdIsNot(updateCategoryRequest.name(), id)) {
            throw new ConflictException(String.format("A category named %s already exists", updateCategoryRequest.name()));
        }

        final Category existingCategory = findById(id);
        existingCategory.setName(updateCategoryRequest.name());
        existingCategory.setDescription(updateCategoryRequest.description());
        return categoryRepository.save(existingCategory);
    }

    public Category addItemToCategory(final Long categoryId, final Long itemId) {
        final Category category = findById(categoryId);
        final Item item = itemService.findById(itemId);

        if(category.getItemsSet().contains(item)){
            throw new ConflictException("Category already has this item");
        }

        category.getItemsSet().add(item);
        return categoryRepository.save(category);
    }

    public Category removeItemFromCategory(final Long categoryId, final Long itemId) {
        final Category category = findById(categoryId);
        final Item item = itemService.findById(itemId);

        if(!category.getItemsSet().contains(item)){
            throw new NotFoundException("Category does not have this item");
        }
        category.getItemsSet().remove(item);
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(final Long categoryId) {
        findById(categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
