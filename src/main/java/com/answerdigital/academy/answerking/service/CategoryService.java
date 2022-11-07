package com.answerdigital.academy.answerking.service;

import com.answerdigital.academy.answerking.exception.ConflictException;
import com.answerdigital.academy.answerking.exception.NotFoundException;
import com.answerdigital.academy.answerking.mapper.CategoryMapper;
import com.answerdigital.academy.answerking.model.Category;
import com.answerdigital.academy.answerking.model.Item;
import com.answerdigital.academy.answerking.repository.CategoryRepository;
import com.answerdigital.academy.answerking.request.AddCategoryRequest;
import com.answerdigital.academy.answerking.request.UpdateCategoryRequest;
import org.mapstruct.factory.Mappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class CategoryService {
    private final ItemService itemService;

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    @Autowired
    public CategoryService(final ItemService itemService,
                           final CategoryRepository categoryRepository) {
        this.itemService = itemService;
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(final AddCategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.name())) {
            throw new ConflictException(String.format("A category named '%s' already exists", categoryRequest.name()));
        }

        final Category newCategory = categoryMapper.addRequestToCategory(categoryRequest);
        return categoryRepository.save(newCategory);
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
            final var exceptionMessage = String.format("A category named %s already exists", updateCategoryRequest.name());
            log.error(exceptionMessage);
            throw new ConflictException(String.format("A category named %s already exists", updateCategoryRequest.name()));
        }

        final Category updatedCategory = categoryMapper.updateRequestToCategory(findById(id), updateCategoryRequest);
        return categoryRepository.save(updatedCategory);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Category addItemToCategory(final Long categoryId, final Long itemId) {
        final Category category = findById(categoryId);
        final Item item = itemService.findById(itemId);

        if (category.getItems().contains(item)) {
            final var exceptionMessage = "Category already has this item";
            log.error(exceptionMessage);
            throw new ConflictException(exceptionMessage);
        }

        category.addItem(item);
        return categoryRepository.save(category);
    }

    public Category removeItemFromCategory(final Long categoryId, final Long itemId) {
        final Category category = findById(categoryId);
        final Item item = itemService.findById(itemId);

        if (!category.getItems().contains(item)) {
            throw new NotFoundException("Category does not have this item");
        }

        category.removeItem(item);
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(final Long categoryId) {
        findById(categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
