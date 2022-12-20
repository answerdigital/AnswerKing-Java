package com.answerdigital.answerking.service;

import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.custom.ProductAlreadyPresentException;
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
     * Creates a Category.
     * @param categoryRequest The CategoryRequest.
     * @return The newly persisted Category, in the form of a CategoryResponse.
     * @throws NameUnavailableException When the Category name already exists.
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
     * Finds a Category by an ID.
     * @param categoryId The ID of the category.
     * @return The found Category.
     * @throws NotFoundException When the category cannot be found.
     */
    public Category findById(final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with ID %d does not exist.", categoryId)));
    }

    /**
     * Finds a Category by ID and maps it to a CategoryResponse object. If
     * no category
     * @param categoryId The ID of the Category.
     * @return The mapped CategoryResponse.
     * @throws NotFoundException When the category cannot be found.
     */
    public CategoryResponse findByIdResponse(final Long categoryId) {
        final Category category = findById(categoryId);
        return categoryToResponse(category);
    }

    /**
     * Finds all the Categories within the database.
     * @return A List of CategoryResponses.
     */
    public Set<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::convertCategoryEntityToCategoryResponse)
                .collect(Collectors.toSet());
    }

    /**
     * Updates a Category
     * @param categoryRequest The CategoryRequest object.
     * @param id The ID of the Category to update.
     * @return The updated Category, in the form of a CategoryResponse.
     * @throws NameUnavailableException When the Category name already exists.
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
     * Retires a Category.
     * @param categoryId The Category ID to retire.
     */
    public void retireCategory(final Long categoryId) {
        final Category category = findById(categoryId);
        if(category.isRetired()) {
            throw new RetirementException(String.format("The category with ID %d is already retired", categoryId));
        }
        category.setRetired(true);
        categoryRepository.save(category);
    }

    /**
     * Finds a List of all the Products within a Category.
     * @param categoryId The ID of the Category.
     * @return A List of ProductResponses.
     */
    public List<ProductResponse> findProductsByCategoryId(final Long categoryId) {
        return productService.findProductsByCategoryId(categoryId);
    }

    /**
     * Adds a Product to a Category.
     * @param categoryId The ID of the Category.
     * @param productId The ID of the Product.
     * @return The newly persisted Category, in the form of a CategoryResponse.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryResponse addProductToCategory(final Long categoryId, final Long productId) {
        final Category category = findById(categoryId);
        final Product product = productService.findById(productId);

        if (category.getProducts().contains(product)) {
            throw new ProductAlreadyPresentException("Category already has this product");
        }

        category.addProduct(product);
        return categoryToResponse(categoryRepository.save(category));
    }

    /**
     * Removes a product from a Category.
     * @param categoryId The ID of the Category.
     * @param productId The ID of the Product.
     * @return The newly persisted Category, in the form of a CategoryResponse.
     */
    public CategoryResponse removeProductFromCategory(final Long categoryId, final Long productId) {
        final Category category = findById(categoryId);
        final Product product = productService.findById(productId);

        if (!category.getProducts().contains(product)) {
            throw new NotFoundException("Category does not have this product");
        }

        category.removeProduct(product);
        return categoryToResponse(categoryRepository.save(category));
    }

    /**
     * Adds a list of products to a given category.
     * @param category The Category entity to add the products to.
     * @param productIds The List of Product Ids.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addProductsToCategory(final Category category, final List<Long> productIds) {
        final Set<Product> products = new HashSet<>(productService.findAllProductsInListOfIds(productIds));
        validateNoProductsAreRetired(products);
        category.setProducts(products);
        categoryRepository.save(category);
    }

    /**
     * Checks if any products within a Set are retired.
     * @param products The Set of Products to check.
     * @throws RetirementException When a product is retired.
     */
    private void validateNoProductsAreRetired(final Set<Product> products) {
        final List<Long> retiredProducts = products
            .stream()
            .filter(Product::isRetired)
            .map(Product::getId)
            .toList();

        if(!retiredProducts.isEmpty()) {
            throw new RetirementException(String.format("Products with IDs %s are retired", retiredProducts));
        }
    }

    /**
     * To be used when validating that the category name does not exist
     * when creating a new category.
     * @param categoryName The Category name to check.
     */
    private void validateCategoryNameDoesNotExistWhenCreating(final String categoryName) {
        if (categoryRepository.existsByName(categoryName)) {
            throw new NameUnavailableException(String.format("A category named '%s' already exists", categoryName));
        }
    }

    /**
     * To be used when validating that the category name does not exist
     * when renaming an existing category.
     * @param categoryName The Category Name to check.
     * @param id The ID of the existing category.
     */
    private void validateCategoryNameDoesNotExistWhenUpdating(final String categoryName, final Long id) {
        if (categoryRepository.existsByNameAndIdIsNot(categoryName, id)) {
            throw new NameUnavailableException(String.format("A category named %s already exists", categoryName));
        }
    }

    /**
     * Updates a Category against a CategoryRequest.
     * @param category The Category to update.
     * @param categoryRequest The CategoryRequest to map.
     * @return The updated Category.
     */
    private Category updateRequestToCategory(final Category category, final CategoryRequest categoryRequest) {
        return categoryMapper.updateRequestToCategory(category, categoryRequest);
    }

    /**
     * Map a Category entity model to a CategoryResponse.
     * @param category The Category entity model to map.
     * @return The mapped CategoryResponse.
     */
    private CategoryResponse categoryToResponse(final Category category) {
        return categoryMapper.convertCategoryEntityToCategoryResponse(category);
    }

    /**
     * Map a CategoryRequest to a Category entity model.
     * @param categoryRequest The CategoryRequest to map.
     * @return The mapped Category entity model.
     */
    private Category requestToCategory(final CategoryRequest categoryRequest) {
        return categoryMapper.addRequestToCategory(categoryRequest);
    }
}
