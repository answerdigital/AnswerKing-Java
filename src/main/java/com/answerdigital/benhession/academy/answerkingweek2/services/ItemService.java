package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.ItemRepository;
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

    public Item addNewItem(Item item) {
        if (itemRepository.existsByName(item.getName())) {
            throw new ConflictException(String.format("An Item named '%s' already exists", item.getName()));
        }
        return itemRepository.save(item);
    }

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with ID %d does not exist.", itemId)));
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item updateItem(Item item) {
        if (itemRepository.existsByNameAndIdIsNot(item.getName(), item.getId())) {
            throw new ConflictException(String.format("An Item named '%s' already exists", item.getName()));
        }
        return itemRepository.save(item);
    }
}
