package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Optional<Item> findById(Long itemId) {
        return itemRepository.findById(itemId);
    }

    public Optional<Item> addNewItem(Item item) {
        if (itemRepository.existsByName(item.getName()))
            throw new ConflictException("Item with this name already exist");

        return Optional.of(itemRepository.save(item));
    }

    public Optional<Item> updateItem(Item item, long itemId) {
        if (itemRepository.existsByNameAndIdIsNot(item.getName(), itemId))
            throw new ConflictException(String.format("An Item named '%s' already exists", item.getName()));

        return Optional.of(itemRepository.save(item));
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

}
