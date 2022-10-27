package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
import com.answerdigital.benhession.academy.answerkingweek2.mappers.ItemMapper;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.ItemRepository;
import com.answerdigital.benhession.academy.answerkingweek2.request.ItemRequest;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper =
            Mappers.getMapper(ItemMapper.class);

    @Autowired
    public ItemService(final ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item addNewItem(final ItemRequest itemRequest) {
        if (itemRepository.existsByName(itemRequest.name())) {
            throw new ConflictException(String.format("An Item named '%s' already exists", itemRequest.name()));
        }

        return itemRepository.save(itemMapper.addRequestToItem(itemRequest));
    }

    public Item findById(final Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with ID %d does not exist.", itemId)));
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item updateItem(final long itemId, final ItemRequest itemRequest) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with ID %d does not exist.", itemId)));
        if (itemRepository.existsByNameAndIdIsNot(itemRequest.name(), itemId)) {
            throw new ConflictException(String.format("An Item named '%s' already exists", itemRequest.name()));
        }
        return itemRepository.save(
                itemMapper.updateRequestToItem(findById(itemId), itemRequest)
        );
    }
}
