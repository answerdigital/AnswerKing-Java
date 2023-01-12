package com.answerdigital.answerking.service;

import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.mapper.ProductMapper;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.repository.ProductRepository;
import com.answerdigital.answerking.request.ProductRequest;
import com.answerdigital.answerking.response.ProductResponse;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final CategoryService categoryService;

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Autowired
    public ProductService(final ProductRepository productRepository, @Lazy final CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public ProductResponse addNewProduct(final ProductRequest productRequest) {
        if (productRepository.existsByName(productRequest.name())) {
            throw new NameUnavailableException(String.format("A Product named '%s' already exists", productRequest.name()));
        }

        final Category category = categoryService.findById(productRequest.categoryId());

        final Product newProduct = productMapper.addRequestToProduct(productRequest);
        newProduct.setCategory(category);
        final Product savedProduct = productRepository.save(newProduct);
        return productMapper.convertProductEntityToProductResponse(savedProduct);
    }

    public Product findById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("Product with ID %d does not exist.", productId)));
    }

    public ProductResponse findByIdResponse(final Long productId) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("Product with ID %d does not exist.", productId)));

        return productMapper.convertProductEntityToProductResponse(product);
    }

    public List<ProductResponse> findAll() {
        final List<Product> productsList = productRepository.findAll();
        return productsList
                .stream()
                .map(productMapper::convertProductEntityToProductResponse)
                .toList();
    }

    public ProductResponse updateProduct(final Long productId, final ProductRequest productRequest) {
        final Product product = findById(productId);

        if (productRepository.existsByNameAndIdIsNot(productRequest.name(), productId)) {
            throw new NameUnavailableException(String.format("A Product named '%s' already exists", productRequest.name()));
        }

        final Product updatedProduct = productMapper.updateRequestToProduct(product, productRequest);
        return productMapper.convertProductEntityToProductResponse(
            productRepository.save(updatedProduct)
        );
    }

    public void retireProduct(final Long productId) {
        final Product product = findById(productId);
        if (product.isRetired()) {
            throw new RetirementException(String.format("The product with ID %d is already retired", productId));
        }
        product.setRetired(true);
        productRepository.save(product);
    }

    public List<ProductResponse> findProductsByCategoryId(final Long categoryId) {
        return productRepository.findProductsByCategoryId(categoryId)
                                .stream()
                                .map(productMapper::convertProductEntityToProductResponse)
                                .toList();
    }

    public List<Product> findAllProductsInListOfIds(final List<Long> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }
}
