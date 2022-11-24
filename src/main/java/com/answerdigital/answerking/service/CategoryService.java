package com.answerdigital.answerking.service;

import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.custom.ProductAlreadyPresentException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.mapper.CategoryMapper;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.repository.CategoryRepository;
import com.answerdigital.answerking.request.AddCategoryRequest;
import com.answerdigital.answerking.request.UpdateCategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

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

    public CategoryResponse addCategory(final AddCategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.name())) {
            throw new NameUnavailableException(String.format("A category named '%s' already exists", categoryRequest.name()));
        }

        final Category newCategory = categoryMapper.addRequestToCategory(categoryRequest);
        final var category =  categoryRepository.save(newCategory);
        return categoryMapper.convertCategoryEntityToCategoryResponse(category);
    }

    public Category findById(final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with ID %d does not exist.", categoryId)));
    }

    public Set<CategoryResponse> findAll() {
        final var categorySet = categoryRepository.findAll();
        return categorySet
                .stream()
                .map(categoryMapper::convertCategoryEntityToCategoryResponse)
                .collect(Collectors.toSet());
    }

    public Category updateCategory(final UpdateCategoryRequest updateCategoryRequest, final Long id) {
        // check that the category isn't being renamed to a category name that already exists
        if (categoryRepository.existsByNameAndIdIsNot(updateCategoryRequest.name(), id)) {
            throw new NameUnavailableException(String.format("A category named %s already exists", updateCategoryRequest.name()));
        }

        final Category updatedCategory = categoryMapper.updateRequestToCategory(findById(id), updateCategoryRequest);
        return categoryRepository.save(updatedCategory);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Category addProductToCategory(final Long categoryId, final Long productId) {
        final Category category = findById(categoryId);
        final Product product = productService.findById(productId);

        if (category.getProducts().contains(product)) {
            throw new ProductAlreadyPresentException("Category already has this product");
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

    public Category retireCategory(final Long categoryId) {
        final Category category = findById(categoryId);
        if(category.isRetired()) {
            throw new RetirementException(String.format("The category with ID %d is already retired", categoryId));
        }
        category.setRetired(true);
        return categoryRepository.save(category);
    }
}
