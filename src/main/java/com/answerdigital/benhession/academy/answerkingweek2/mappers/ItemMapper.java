package com.answerdigital.benhession.academy.answerkingweek2.mappers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.request.ItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item addRequestToItem(ItemRequest itemRequest);

    Item updateRequestToItem(@MappingTarget Item item, ItemRequest itemRequest);
}
