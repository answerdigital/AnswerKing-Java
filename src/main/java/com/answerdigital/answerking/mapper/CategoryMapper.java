package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.request.AddCategoryRequest;
import com.answerdigital.answerking.request.UpdateCategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
import com.answerdigital.answerking.util.DateTimeUtility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        imports = {DateTimeUtility.class, Collectors.class})
public interface CategoryMapper {

    @Mapping(target = "createdOn", expression = "java(DateTimeUtility.getDateTimeAsString())")
    @Mapping(target = "lastUpdated", expression = "java(DateTimeUtility.getDateTimeAsString())")
    Category addRequestToCategory(AddCategoryRequest addCategoryRequest);

    @Mapping(target = "lastUpdated", expression = "java(DateTimeUtility.getDateTimeAsString())")
    Category updateRequestToCategory(@MappingTarget Category category, UpdateCategoryRequest updateCategoryRequest);

    @Mapping(target = "productIds",
             expression = "java(category.getProducts().stream().map(product -> product.getId()).collect(Collectors.toList()) )")
    CategoryResponse convertCategoryEntityToCategoryResponse(Category category);

}
