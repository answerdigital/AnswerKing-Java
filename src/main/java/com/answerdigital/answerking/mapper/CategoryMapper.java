package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.request.AddCategoryRequest;
import com.answerdigital.answerking.request.UpdateCategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring", imports = {DateTimeFormatter.class, ZoneOffset.class, ZonedDateTime.class, ChronoUnit.class})
public interface CategoryMapper {

    @Mapping(target = "createdOn", expression = "java( ZonedDateTime.now(ZoneOffset.UTC)" +
                                                    ".truncatedTo( ChronoUnit.SECONDS )" +
                                                    ".format( DateTimeFormatter.ofPattern( \"yyyy-MM-dd HH:mm:ss\" ) ) )")
    @Mapping(target = "lastUpdated", expression = "java( ZonedDateTime.now(ZoneOffset.UTC)" +
                                                        ".truncatedTo( ChronoUnit.SECONDS )" +
                                                        ".format( DateTimeFormatter.ofPattern( \"yyyy-MM-dd HH:mm:ss\" ) ) )")
    Category addRequestToCategory(AddCategoryRequest addCategoryRequest);

    @Mapping(target = "lastUpdated", expression = "java( ZonedDateTime.now(ZoneOffset.UTC)" +
                                                    ".truncatedTo( ChronoUnit.SECONDS )" +
                                                    ".format( DateTimeFormatter.ofPattern( \"yyyy-MM-dd HH:mm:ss\" ) ) )")
    Category updateRequestToCategory(@MappingTarget Category category, UpdateCategoryRequest updateCategoryRequest);
}
