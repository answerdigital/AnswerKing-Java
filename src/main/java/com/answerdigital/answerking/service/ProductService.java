package com.answerdigital.answerking.service;

import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.mapper.ProductMapper;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.repository.ProductRepository;
import com.answerdigital.answerking.request.ProductRequest;
import com.answerdigital.answerking.response.ProductResponse;
import com.answerdigital.answerking.response.SimpleProductResponse;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.PRODUCTS_ALREADY_EXIST;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.PRODUCTS_ARE_RETIRED;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.PRODUCTS_DO_NOT_EXIST;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.getCustomException;

/**
 * The class ProductService is the service layer for Products {@link com.answerdigital.answerking.model.Product}.
 * It services requests from
 * ProductController {@link com.answerdigital.answerking.controller.ProductController} and
 * interacts with ProductRepository {@link com.answerdigital.answerking.repository.ProductRepository}
 * to connect with the database.
 */
@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    private final CategoryService categoryService;

    @Autowired
    public ProductService(
            final ProductRepository productRepository,
            @Lazy final CategoryService categoryService
    ) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    /**
     * Creates a Product {@link com.answerdigital.answerking.model.Product}
     * and adds it to a Category {@link com.answerdigital.answerking.model.Category}.
     * @param productRequest The ProductRequest {@link com.answerdigital.answerking.request.ProductRequest}.
     * @return The newly persisted Product {@link com.answerdigital.answerking.model.Product},
     * in the form of a ProductResponse {@link com.answerdigital.answerking.response.ProductResponse}.
     * @throws NameUnavailableException When the Product {@link com.answerdigital.answerking.model.Product}
     * name already exists.
     */
    public ProductResponse addNewProduct(final ProductRequest productRequest) {
        if (productRepository.existsByName(productRequest.name())) {
            throw getCustomException(PRODUCTS_ALREADY_EXIST, productRequest.name());
        }

        final Category category = categoryService.findById(productRequest.categoryId());

        final Product newProduct = productMapper.addRequestToProduct(productRequest);
        newProduct.setCategory(category);

        final Product savedProduct = productRepository.save(newProduct);
        return productMapper.convertProductEntityToProductResponse(savedProduct);
    }

    /**
     * Finds a Product {@link com.answerdigital.answerking.model.Product} by an ID.
     * @param productId The ID of the Product {@link com.answerdigital.answerking.model.Product}.
     * @return The found Product {@link com.answerdigital.answerking.model.Product}.
     * @throws NotFoundException When the product {@link com.answerdigital.answerking.model.Product}
     * cannot be found.
     */
    public Product findById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> getCustomException(PRODUCTS_DO_NOT_EXIST, productId));
    }

    /**
     * Finds a Product {@link com.answerdigital.answerking.model.Product} by ID
     * and maps it to a ProductResponse {@link com.answerdigital.answerking.response.ProductResponse} object.
     * @param productId The ID of the Product {@link com.answerdigital.answerking.model.Product}.
     * @return The mapped ProductResponse {@link com.answerdigital.answerking.response.ProductResponse}.
     * @throws NotFoundException When the Product {@link com.answerdigital.answerking.model.Product} cannot be found.
     */
    public ProductResponse findByIdResponse(final Long productId) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> getCustomException(PRODUCTS_DO_NOT_EXIST, productId));

        return productMapper.convertProductEntityToProductResponse(product);
    }

    /**
     * Finds all the Products {@link com.answerdigital.answerking.model.Product} within the database.
     * @return A List of ProductResponses {@link com.answerdigital.answerking.response.ProductResponse}.
     */
    public List<ProductResponse> findAll() {
        final List<Product> productsList = productRepository.findAll();
        return productsList
                .stream()
                .map(productMapper::convertProductEntityToProductResponse)
                .toList();
    }

    /**
     * Updates a Product {@link com.answerdigital.answerking.model.Product}.
     * @param productRequest The ProductRequest object {@link com.answerdigital.answerking.request.ProductRequest}.
     * @param productId The ID of the Product {@link com.answerdigital.answerking.model.Product} to update.
     * @return The updated Product {@link com.answerdigital.answerking.model.Product},
     * in the form of a ProductResponse {@link com.answerdigital.answerking.response.ProductResponse}.
     * @throws NameUnavailableException When the Product {@link com.answerdigital.answerking.model.Product} name already exists.
     */
    public ProductResponse updateProduct(final Long productId, final ProductRequest productRequest) {
        final Product product = findById(productId);

        if (productRepository.existsByNameAndIdIsNot(productRequest.name(), productId)) {
            throw getCustomException(PRODUCTS_ALREADY_EXIST, productRequest.name());
        }

        final Product updatedProduct = productMapper.updateRequestToProduct(product, productRequest);
        return productMapper.convertProductEntityToProductResponse(
            productRepository.save(updatedProduct)
        );
    }

    /**
     * Retires a Product {@link com.answerdigital.answerking.model.Product}.
     * @param productId The Product ID to retire.
     */
    public void retireProduct(final Long productId) {
        final Product product = findById(productId);

        if (product.isRetired()) {
            throw getCustomException(PRODUCTS_ARE_RETIRED, productId);
        }

        product.setRetired(true);
        productRepository.save(product);
    }

    /**
     * Finds a List of all the Products {@link com.answerdigital.answerking.model.Product}
     * within a Category {@link com.answerdigital.answerking.model.Category}.
     * @param categoryId The ID of the Category {@link com.answerdigital.answerking.model.Category}.
     * @return A List of ProductResponses {@link com.answerdigital.answerking.response.ProductResponse}.
     */
    public List<SimpleProductResponse> findProductsByCategoryId(final Long categoryId) {
        return productRepository.findProductsByCategoryId(categoryId)
                                .stream()
                                .map(productMapper::convertProductEntityToSimpleProductResponse)
                                .toList();
    }

    /**
     * Finds a List of Products {@link com.answerdigital.answerking.model.Product}
     * matching the provided Product IDs.
     * @param productIds A List of Product IDs.
     * @return A List of Products {@link com.answerdigital.answerking.model.Product}
     * matching the provided Product IDs.
     */
    public List<Product> findAllProductsInListOfIds(final List<Long> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }
}
