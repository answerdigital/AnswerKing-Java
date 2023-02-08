package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.request.ProductRequest;
import com.answerdigital.answerking.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * The class ProductMapper is the mapping layer for Products {@link com.answerdigital.answerking.model.Product}.
 * It handles interactions with ProductRequest {@link com.answerdigital.answerking.request.ProductRequest}
 * and ProductResponse {@link com.answerdigital.answerking.response.ProductResponse}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "retired", constant = "false")
    Product addRequestToProduct(ProductRequest productRequest);

    Product updateRequestToProduct(@MappingTarget Product product, ProductRequest productRequest);

    @Mapping(source = "category.id", target = "category")
    ProductResponse convertProductEntityToProductResponse(Product product);

}
