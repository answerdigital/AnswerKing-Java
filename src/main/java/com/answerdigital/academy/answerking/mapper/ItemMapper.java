package com.answerdigital.academy.answerking.mapper;

import com.answerdigital.academy.answerking.model.Item;
import com.answerdigital.academy.answerking.request.ItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item addRequestToItem(ItemRequest itemRequest);

    Item updateRequestToItem(@MappingTarget Item item, ItemRequest itemRequest);
}
