package com.answerdigital.academy.answerking.service;

import com.answerdigital.academy.answerking.exception.generic.ConflictException;
import com.answerdigital.academy.answerking.exception.generic.NotFoundException;
import com.answerdigital.academy.answerking.mapper.CategoryMapper;
import com.answerdigital.academy.answerking.model.Category;
import com.answerdigital.academy.answerking.model.Product;
import com.answerdigital.academy.answerking.repository.CategoryRepository;
import com.answerdigital.academy.answerking.request.AddCategoryRequest;
import com.answerdigital.academy.answerking.request.UpdateCategoryRequest;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class CategoryService {
    private final ProductService productService;

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    @Autowired
    public CategoryService(final ProductService productService,
                           final CategoryRepository categoryRepository) {
        this.productService = productService;
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
            throw new ConflictException(String.format("A category named %s already exists", updateCategoryRequest.name()));
        }

        final Category updatedCategory = categoryMapper.updateRequestToCategory(findById(id), updateCategoryRequest);
        return categoryRepository.save(updatedCategory);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Category addProductToCategory(final Long categoryId, final Long productId) {
        final Category category = findById(categoryId);
        final Product product = productService.findById(productId);

        if (category.getProducts().contains(product)) {
            throw new ConflictException("Category already has this product");
        }

        category.addProduct(product);
        return categoryRepository.save(category);
    }

    public Category removeProductFromCategory(final Long categoryId, final Long productId) {
        final Category category = findById(categoryId);
        final Product product = productService.findById(productId);

        if (!category.getProducts().contains(product)) {
            throw new NotFoundException("Category does not have this product");
        }

        category.removeProduct(product);
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(final Long categoryId) {
        findById(categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
