package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.request.AddCategoryRequest;
import com.answerdigital.answerking.request.UpdateCategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category addRequestToCategory(AddCategoryRequest addCategoryRequest);

    Category updateRequestToCategory(@MappingTarget Category category, UpdateCategoryRequest updateCategoryRequest);
}
