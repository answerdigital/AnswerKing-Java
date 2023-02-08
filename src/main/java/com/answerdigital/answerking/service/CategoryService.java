package com.answerdigital.answerking.service;

import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.mapper.CategoryMapper;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.repository.CategoryRepository;
import com.answerdigital.answerking.request.CategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
import com.answerdigital.answerking.response.ProductResponse;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.CATEGORIES_ALREADY_EXIST;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.CATEGORIES_ARE_RETIRED;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.CATEGORIES_DO_NOT_EXIST;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.PRODUCTS_ARE_RETIRED;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.getCustomException;

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

    /**
     * Creates a Category {@link com.answerdigital.answerking.model.Category}.
     * @param categoryRequest The CategoryRequest {@link com.answerdigital.answerking.request.CategoryRequest} object.
     * @return The newly persisted Category {@link com.answerdigital.answerking.model.Category},
     * in the form of a CategoryResponse {@link com.answerdigital.answerking.response.CategoryResponse}.
     * @throws NameUnavailableException When the Category {@link com.answerdigital.answerking.model.Category}
     * name already exists.
     */
    @Transactional
    public CategoryResponse addCategory(final CategoryRequest categoryRequest) {
        validateCategoryNameDoesNotExistWhenCreating(categoryRequest.name());

        // Create a new category from the Request and persist the initial category.
        Category category = requestToCategory(categoryRequest);
        category =  categoryRepository.save(category);

        // Add the products to the category.
        addProductsToCategory(category, categoryRequest.productIds());

        return categoryToResponse(category);
    }

    /**
     * Finds a Category {@link com.answerdigital.answerking.model.Category} by an ID.
     * @param categoryId The ID of the Category {@link com.answerdigital.answerking.model.Category}.
     * @return The found Category {@link com.answerdigital.answerking.model.Category}.
     * @throws NotFoundException When the Category {@link com.answerdigital.answerking.model.Category}
     * cannot be found.
     */
    public Category findById(final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> getCustomException(CATEGORIES_DO_NOT_EXIST, categoryId));
    }

    /**
     * Finds a Category {@link com.answerdigital.answerking.model.Category} by ID and maps it to
     * a CategoryResponse {@link com.answerdigital.answerking.response.CategoryResponse} object.
     * @param categoryId The ID of the Category {@link com.answerdigital.answerking.model.Category}.
     * @return The mapped CategoryResponse {@link com.answerdigital.answerking.response.CategoryResponse}.
     * @throws NotFoundException When the Category {@link com.answerdigital.answerking.model.Category}
     * cannot be found.
     */
    public CategoryResponse findByIdResponse(final Long categoryId) {
        final Category category = findById(categoryId);
        return categoryToResponse(category);
    }

    /**
     * Finds all the Categories {@link com.answerdigital.answerking.model.Category} within the database.
     * @return A List of CategoryResponses {@link com.answerdigital.answerking.response.CategoryResponse}.
     */
    public Set<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::convertCategoryEntityToCategoryResponse)
                .collect(Collectors.toSet());
    }

    /**
     * Updates a Category {@link com.answerdigital.answerking.model.Category}.
     * @param categoryRequest The CategoryRequest {@link com.answerdigital.answerking.request.CategoryRequest} object.
     * @param id The ID of the Category {@link com.answerdigital.answerking.model.Category} to update.
     * @return The updated Category {@link com.answerdigital.answerking.model.Category},
     * in the form of a CategoryResponse {@link com.answerdigital.answerking.response.CategoryResponse}.
     * @throws NameUnavailableException When the Category {@link com.answerdigital.answerking.model.Category}
     * name already exists.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryResponse updateCategory(final CategoryRequest categoryRequest, final Long id) {
        validateCategoryNameDoesNotExistWhenUpdating(categoryRequest.name(), id);

        final Category category = findById(id);
        final Category updatedCategory = updateRequestToCategory(category, categoryRequest);
        addProductsToCategory(updatedCategory, categoryRequest.productIds());

        return categoryToResponse(categoryRepository.save(updatedCategory));
    }

    /**
     * Retires a Category {@link com.answerdigital.answerking.model.Category}.
     * @param categoryId The Category {@link com.answerdigital.answerking.model.Category} ID to retire.
     */
    public void retireCategory(final Long categoryId) {
        final Category category = findById(categoryId);

        if(category.isRetired()) {
            throw getCustomException(CATEGORIES_ARE_RETIRED, categoryId);
        }

        category.setRetired(true);
        categoryRepository.save(category);
    }

    /**
     * Finds a List of all the Products {@link com.answerdigital.answerking.model.Product}
     * within a Category {@link com.answerdigital.answerking.model.Category}.
     * @param categoryId The ID of the Category {@link com.answerdigital.answerking.model.Category}.
     * @return A List of ProductResponses {@link com.answerdigital.answerking.response.ProductResponse}.
     */
    public List<ProductResponse> findProductsByCategoryId(final Long categoryId) {
        return productService.findProductsByCategoryId(categoryId);
    }

    /**
     * Adds a List of Products {@link com.answerdigital.answerking.model.Product}
     * to a given Category {@link com.answerdigital.answerking.model.Category}.
     * @param category The Category {@link com.answerdigital.answerking.model.Category} object
     * to add the Products {@link com.answerdigital.answerking.model.Product} to.
     * @param productIds The List of Product {@link com.answerdigital.answerking.model.Product} IDs.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addProductsToCategory(final Category category, final List<Long> productIds) {
        final Set<Product> products = new HashSet<>(productService.findAllProductsInListOfIds(productIds));
        validateNoProductsAreRetired(products);
        category.setProducts(products);
        categoryRepository.save(category);
    }

    /**
     * Checks if any Products {@link com.answerdigital.answerking.model.Product}
     * within a Set are retired.
     * @param products The Set of Products {@link com.answerdigital.answerking.model.Product} to check.
     * @throws RetirementException When a Product {@link com.answerdigital.answerking.model.Product} is retired.
     */
    private void validateNoProductsAreRetired(final Set<Product> products) {
        final List<Long> retiredProducts = products
            .stream()
            .filter(Product::isRetired)
            .map(Product::getId)
            .toList();

        if(!retiredProducts.isEmpty()) {
            throw getCustomException(PRODUCTS_ARE_RETIRED, retiredProducts);
        }
    }

    /**
     * To be used when validating that the Category {@link com.answerdigital.answerking.model.Category}
     * name does not exist when creating a new category.
     * @param categoryName The Category {@link com.answerdigital.answerking.model.Category}
     * name to check.
     */
    private void validateCategoryNameDoesNotExistWhenCreating(final String categoryName) {
        if (categoryRepository.existsByName(categoryName)) {
            throw getCustomException(CATEGORIES_ALREADY_EXIST, categoryName);
        }
    }

    /**
     * To be used when validating that the Category {@link com.answerdigital.answerking.model.Category}
     * name does not exist when renaming an existing Category {@link com.answerdigital.answerking.model.Category}.
     * @param categoryName The Category {@link com.answerdigital.answerking.model.Category} name to check.
     * @param id The ID of the existing Category {@link com.answerdigital.answerking.model.Category}.
     */
    private void validateCategoryNameDoesNotExistWhenUpdating(final String categoryName, final Long id) {
        if (categoryRepository.existsByNameAndIdIsNot(categoryName, id)) {
            throw getCustomException(CATEGORIES_ALREADY_EXIST, categoryName);
        }
    }

    /**
     * Updates a Category {@link com.answerdigital.answerking.model.Category}
     * against a CategoryRequest {@link com.answerdigital.answerking.request.CategoryRequest}.
     * @param category The Category {@link com.answerdigital.answerking.model.Category} to update.
     * @param categoryRequest The CategoryRequest {@link com.answerdigital.answerking.request.CategoryRequest}
     * to map.
     * @return The updated Category {@link com.answerdigital.answerking.model.Category}.
     */
    private Category updateRequestToCategory(final Category category, final CategoryRequest categoryRequest) {
        return categoryMapper.updateRequestToCategory(category, categoryRequest);
    }

    /**
     * Map a Category {@link com.answerdigital.answerking.model.Category}
     * to a CategoryResponse {@link com.answerdigital.answerking.response.CategoryResponse}.
     * @param category The Category {@link com.answerdigital.answerking.model.Category} to map.
     * @return The mapped CategoryResponse {@link com.answerdigital.answerking.response.CategoryResponse}.
     */
    private CategoryResponse categoryToResponse(final Category category) {
        return categoryMapper.convertCategoryEntityToCategoryResponse(category);
    }

    /**
     * Map a CategoryRequest {@link com.answerdigital.answerking.request.CategoryRequest}
     * to a Category {@link com.answerdigital.answerking.model.Category}
     * @param categoryRequest The CategoryRequest {@link com.answerdigital.answerking.request.CategoryRequest}
     * to map.
     * @return The mapped Category {@link com.answerdigital.answerking.model.Category}.
     */
    private Category requestToCategory(final CategoryRequest categoryRequest) {
        return categoryMapper.addRequestToCategory(categoryRequest);
    }

}
