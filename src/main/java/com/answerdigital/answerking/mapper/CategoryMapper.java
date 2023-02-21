package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.request.CategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
import com.answerdigital.answerking.response.SimpleCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;

/**
 * The class CategoryMapper is the mapping layer for Categories {@link com.answerdigital.answerking.model.Category}.
 * It handles interactions with CategoryRequest {@link com.answerdigital.answerking.request.CategoryRequest}
 * and CategoryResponse {@link com.answerdigital.answerking.response.CategoryResponse}.
 */
@Mapper(componentModel = "spring", imports = {Collections.class})
public interface CategoryMapper {

    @Mapping(target = "products", expression = "java(Collections.EMPTY_LIST)")
    Category addRequestToCategory(CategoryRequest addCategoryRequest);

    Category updateRequestToCategory(@MappingTarget Category category, CategoryRequest updateCategoryRequest);

    @Mapping(target = "productIds", expression = "java(MapperExpressionUtility.mapProductIdsFromCategory(category))")
    CategoryResponse convertCategoryEntityToCategoryResponse(Category category);

    SimpleCategoryResponse categoryToSimpleCategoryResponse(Category category);

}
