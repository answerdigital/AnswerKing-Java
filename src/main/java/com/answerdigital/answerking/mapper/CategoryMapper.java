package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.request.CategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
import com.answerdigital.answerking.util.DateTimeUtility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        imports = {DateTimeUtility.class, Collectors.class, Collections.class})
public interface CategoryMapper {

    @Mapping(target = "createdOn", expression = "java(DateTimeUtility.getDateTimeAsString())")
    @Mapping(target = "lastUpdated", expression = "java(DateTimeUtility.getDateTimeAsString())")
    @Mapping(target = "products", expression = "java(Collections.EMPTY_SET)")
    Category addRequestToCategory(CategoryRequest addCategoryRequest);

    @Mapping(target = "lastUpdated", expression = "java(DateTimeUtility.getDateTimeAsString())")
    Category updateRequestToCategory(@MappingTarget Category category, CategoryRequest updateCategoryRequest);

    @Mapping(target = "products",
             expression = "java(category.getProducts().stream().map(product -> product.getId()).collect(Collectors.toList()) )")
    CategoryResponse convertCategoryEntityToCategoryResponse(Category category);

}
