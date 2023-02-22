package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Tag;
import com.answerdigital.answerking.request.TagRequest;
import com.answerdigital.answerking.response.TagResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag addRequestToTag(TagRequest tagRequest);

    Tag updateRequestToTag(@MappingTarget Tag tag, TagRequest tagRequest);

    @Mapping(target = "productIds", expression = "java(MapperExpressionUtility.mapProductIdsFromTag(tag))")
    TagResponse convertTagEntityToTagResponse(Tag tag);

}
