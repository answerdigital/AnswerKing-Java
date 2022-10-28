package com.answerdigital.benhession.academy.answerkingweek2.mappers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import com.answerdigital.benhession.academy.answerkingweek2.request.AddCategoryRequest;
import com.answerdigital.benhession.academy.answerkingweek2.request.UpdateCategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category addRequestToCategory(AddCategoryRequest addCategoryRequest);

    Category updateRequestToCategory(@MappingTarget Category category, UpdateCategoryRequest updateCategoryRequest);
}
