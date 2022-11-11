package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Item;
import com.answerdigital.answerking.request.ItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item addRequestToItem(ItemRequest itemRequest);

    Item updateRequestToItem(@MappingTarget Item item, ItemRequest itemRequest);
}
