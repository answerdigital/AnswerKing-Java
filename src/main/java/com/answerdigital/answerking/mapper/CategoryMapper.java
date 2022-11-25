package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.request.RequestModelsCategory;
import com.answerdigital.answerking.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        imports = {DateTimeFormatter.class, ZoneOffset.class, ZonedDateTime.class, ChronoUnit.class, Collectors.class})
public interface CategoryMapper {
    @Mapping(target = "createdOn", expression = "java( ZonedDateTime.now(ZoneOffset.UTC)" +
                                                    ".truncatedTo( ChronoUnit.SECONDS )" +
                                                    ".format( DateTimeFormatter.ofPattern( \"yyyy-MM-dd HH:mm:ss\" ) ) )")
    @Mapping(target = "lastUpdated", expression = "java( ZonedDateTime.now(ZoneOffset.UTC)" +
                                                        ".truncatedTo( ChronoUnit.SECONDS )" +
                                                        ".format( DateTimeFormatter.ofPattern( \"yyyy-MM-dd HH:mm:ss\" ) ) )")
    Category addRequestToCategory(RequestModelsCategory addCategoryRequest);

    @Mapping(target = "lastUpdated", expression = "java( ZonedDateTime.now(ZoneOffset.UTC)" +
                                                    ".truncatedTo( ChronoUnit.SECONDS )" +
                                                    ".format( DateTimeFormatter.ofPattern( \"yyyy-MM-dd HH:mm:ss\" ) ) )")
    Category updateRequestToCategory(@MappingTarget Category category, RequestModelsCategory updateCategoryRequest);

    @Mapping(target = "productIds",
             expression = "java(category.getProducts().stream().map(product -> product.getId()).collect(Collectors.toList()) )")
    CategoryResponse convertCategoryEntityToCategoryResponse(Category category);
}
