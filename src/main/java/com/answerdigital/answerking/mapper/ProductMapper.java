package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.request.ProductRequest;
import com.answerdigital.answerking.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        imports = {Collectors.class, Collections.class})
public interface ProductMapper {
    @Mapping(target = "retired", constant = "false")
    @Mapping(target = "categories", expression = "java(Collections.EMPTY_SET)")
    Product addRequestToProduct(ProductRequest productRequest);

    Product updateRequestToProduct(@MappingTarget Product product, ProductRequest productRequest);

    @Mapping(target = "categories",
            expression = "java(product.getCategories().stream().map(category -> category.getId()).collect(Collectors.toList()) )")
    ProductResponse convertProductEntityToProductResponse(Product product);
}
