package com.answerdigital.answerking.service;

import com.answerdigital.answerking.exception.generic.ConflictException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.mapper.ItemMapper;
import com.answerdigital.answerking.model.Item;
import com.answerdigital.answerking.repository.ItemRepository;
import com.answerdigital.answerking.request.ItemRequest;
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

        final Item newItem = itemMapper.addRequestToItem(itemRequest);
        return itemRepository.save(newItem);
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

        final Item updatedItem = itemMapper.updateRequestToItem(findById(itemId), itemRequest);
        return itemRepository.save(updatedItem);
    }
}
