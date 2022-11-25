package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.request.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "retired", constant = "false")
    Product addRequestToProduct(ProductRequest productRequest);

    Product updateRequestToProduct(@MappingTarget Product product, ProductRequest productRequest);
}
