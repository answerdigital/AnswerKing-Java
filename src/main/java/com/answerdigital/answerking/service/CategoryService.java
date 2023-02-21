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

import com.answerdigital.answerking.response.SimpleProductResponse;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.CATEGORIES_ALREADY_EXIST;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.CATEGORIES_ARE_RETIRED;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.CATEGORIES_DO_NOT_EXIST;

import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.getCustomException;

/**
 * The class CategoryService is the service layer for Categories {@link com.answerdigital.answerking.model.Category}.
 * It services requests from
 * CategoryController {@link com.answerdigital.answerking.controller.CategoryController} and
 * interacts with CategoryRepository {@link com.answerdigital.answerking.repository.CategoryRepository}
 * to connect with the database.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    private final ProductService productService;

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
    public CategoryResponse addCategory(final CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.name())) {
            throw getCustomException(CATEGORIES_ALREADY_EXIST, categoryRequest.name());
        }

        final Category category = requestToCategory(categoryRequest);
        addProductsToCategory(category, categoryRequest.productIds());

        return categoryToResponse(categoryRepository.save(category));
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
    public CategoryResponse updateCategory(final CategoryRequest categoryRequest, final Long id) {
        if (categoryRepository.existsByNameAndIdIsNot(categoryRequest.name(), id)) {
            throw getCustomException(CATEGORIES_ALREADY_EXIST, categoryRequest.name());
        }

        final Category category = findById(id);
        validateCategoryIsNotRetired(category);

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
        validateCategoryIsNotRetired(category);
        category.setRetired(true);
        categoryRepository.save(category);
    }

    /**
     * Finds a List of all the Products {@link com.answerdigital.answerking.model.Product}
     * within a Category {@link com.answerdigital.answerking.model.Category}.
     * @param categoryId The ID of the Category {@link com.answerdigital.answerking.model.Category}.
     * @return A List of ProductResponses {@link com.answerdigital.answerking.response.ProductResponse}.
     */
    public List<SimpleProductResponse> findProductsByCategoryId(final Long categoryId) {
        return productService.findProductsByCategoryId(categoryId);
    }

    /**
     * Checks if a category is retired {@link com.answerdigital.answerking.model.Category}
     * @param category the Category to validate the retirement of {@link com.answerdigital.answerking.model.Category}.
     * @throws RetirementException When a Category {@link com.answerdigital.answerking.model.Category} is retired.
     */
    private void validateCategoryIsNotRetired(final Category category) {
        if (category.isRetired()) {
            throw getCustomException(CATEGORIES_ARE_RETIRED, category.getId());
        }
    }

    /**
     * Adds a List of Products {@link com.answerdigital.answerking.model.Product}
     * to a given Category {@link com.answerdigital.answerking.model.Category}.
     * @param category The Category {@link com.answerdigital.answerking.model.Category} object
     * to add the Products {@link com.answerdigital.answerking.model.Product} to.
     * @param productIds The List of Product {@link com.answerdigital.answerking.model.Product} IDs.
     */
    private void addProductsToCategory(final Category category, final List<Long> productIds) {
        final List<Product> products = productService.getProductsFromProductIds(productIds);
        category.setProducts(products);
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
