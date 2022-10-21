package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.ItemRepository;
import com.answerdigital.benhession.academy.answerkingweek2.request.ItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item addNewItem(ItemRequest itemRequest) {
        if (itemRepository.existsByName(itemRequest.name())) {
            throw new ConflictException(String.format("An Item named '%s' already exists", itemRequest.name()));
        }
        Item newItem = new Item(itemRequest);
        return itemRepository.save(newItem);
    }

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with ID %d does not exist.", itemId)));
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item updateItem(long itemId, ItemRequest itemRequest) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with ID %d does not exist.", itemId)));
        if (itemRepository.existsByNameAndIdIsNot(itemRequest.name(), itemId)) {
            throw new ConflictException(String.format("An Item named '%s' already exists", itemRequest.name()));
        }
        Item updatedItem = new Item(itemRequest);
        updatedItem.setId(itemId);

        return itemRepository.save(updatedItem);
    }
}
