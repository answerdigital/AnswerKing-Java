package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.request.ProductRequest;
import com.answerdigital.answerking.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;

@Mapper(componentModel = "spring", imports = {Collections.class})
public interface ProductMapper {
    @Mapping(target = "retired", constant = "false")
    @Mapping(target = "tags", expression = "java(Collections.EMPTY_SET)")
    Product addRequestToProduct(ProductRequest productRequest);

    Product updateRequestToProduct(@MappingTarget Product product, ProductRequest productRequest);

    @Mapping(source = "category.id", target = "category")
    @Mapping(target = "tagIds", expression = "java(MapperExpressionUtility.mapTagIdsFromProduct(product))")
    ProductResponse convertProductEntityToProductResponse(Product product);

}
