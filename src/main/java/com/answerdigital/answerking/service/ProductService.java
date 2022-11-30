package com.answerdigital.answerking.service;

import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.mapper.ProductMapper;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.repository.ProductRepository;
import com.answerdigital.answerking.request.ProductRequest;
import com.answerdigital.answerking.response.ProductResponse;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Autowired
    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addNewProduct(final ProductRequest productRequest) {
        if (productRepository.existsByName(productRequest.name())) {
            throw new NameUnavailableException(String.format("A Product named '%s' already exists", productRequest.name()));
        }

        final Product newProduct = productMapper.addRequestToProduct(productRequest);
        return productRepository.save(newProduct);
    }

    public Product findById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("Product with ID %d does not exist.", productId)));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product updateProduct(final Long productId, final ProductRequest productRequest) {
        productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("Product with ID %d does not exist.", productId)));

        if (productRepository.existsByNameAndIdIsNot(productRequest.name(), productId)) {
            throw new NameUnavailableException(String.format("A Product named '%s' already exists", productRequest.name()));
        }

        final Product updatedProduct = productMapper.updateRequestToProduct(findById(productId), productRequest);
        return productRepository.save(updatedProduct);
    }

    public Product retireProduct(final Long productId) {
        final Product product = findById(productId);
        if (product.isRetired()) {
            throw new RetirementException(String.format("The product with ID %d is already retired", productId));
        }
        product.setRetired(true);
        return productRepository.save(product);
    }

    public List<ProductResponse> findProductsByCategoryId(final Long categoryId) {
        return productRepository.findProductsByCategoryId(categoryId)
                                .stream()
                                .map(product -> productMapper.convertProductEntityToProductResponse(product))
                                .toList();
    }

}
