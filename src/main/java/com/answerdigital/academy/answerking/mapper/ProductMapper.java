package com.answerdigital.academy.answerking.mapper;

import com.answerdigital.academy.answerking.model.Product;
import com.answerdigital.academy.answerking.request.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product addRequestToProduct(ProductRequest productRequest);

    Product updateRequestToProduct(@MappingTarget Product product, ProductRequest productRequest);
}
