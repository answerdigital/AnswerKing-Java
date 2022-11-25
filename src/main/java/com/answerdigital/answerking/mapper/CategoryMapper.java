package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.request.RequestModelsCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category addRequestToCategory(RequestModelsCategory addCategoryRequest);

    Category updateRequestToCategory(@MappingTarget Category category, RequestModelsCategory updateCategoryRequest);
}
