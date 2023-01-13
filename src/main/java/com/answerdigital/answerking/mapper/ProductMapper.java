package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.request.ProductRequest;
import com.answerdigital.answerking.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {List.class, Collectors.class})
public interface ProductMapper {
    @Mapping(target = "retired", constant = "false")
    Product addRequestToProduct(ProductRequest productRequest);

    Product updateRequestToProduct(@MappingTarget Product product, ProductRequest productRequest);

    @Mapping(target = "category.productIds",
            expression = "java(category.getProducts().stream().map(product -> product.getId()).collect(Collectors.toList()) )")
    ProductResponse convertProductEntityToProductResponse(Product product);
}
