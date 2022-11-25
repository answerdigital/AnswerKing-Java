package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.request.RequestModelsProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "retired", constant = "false")
    Product addRequestToProduct(RequestModelsProduct productRequest);

    Product updateRequestToProduct(@MappingTarget Product product, RequestModelsProduct productRequest);
}
