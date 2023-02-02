package com.answerdigital.answerking.utility;

import com.answerdigital.answerking.mapper.TagMapper;
import com.answerdigital.answerking.model.Tag;
import com.answerdigital.answerking.response.TagResponse;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

public final class MappingUtility {
    private static final TagMapper tagMapper = Mappers.getMapper(TagMapper.class);

    public static List<TagResponse> allTagsToResponse(Tag ...tags) {
        return Arrays.stream(tags)
            .map(tagMapper::convertTagEntityToTagResponse)
            .toList();
    }
}
