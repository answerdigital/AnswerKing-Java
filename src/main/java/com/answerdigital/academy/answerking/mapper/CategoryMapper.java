package com.answerdigital.academy.answerking.mapper;

import com.answerdigital.academy.answerking.model.Category;
import com.answerdigital.academy.answerking.request.AddCategoryRequest;
import com.answerdigital.academy.answerking.request.UpdateCategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category addRequestToCategory(AddCategoryRequest addCategoryRequest);

    Category updateRequestToCategory(@MappingTarget Category category, UpdateCategoryRequest updateCategoryRequest);
}
