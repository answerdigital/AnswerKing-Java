package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
import com.answerdigital.benhession.academy.answerkingweek2.mappers.ItemMapper;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.ItemRepository;
import com.answerdigital.benhession.academy.answerkingweek2.request.ItemRequest;
import org.mapstruct.factory.Mappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
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
            final var exceptionMessage = String.format("An Item named '%s' already exists", itemRequest.name());
            log.error(exceptionMessage);
            throw new ConflictException(exceptionMessage);
        }

        final Item newItem = itemMapper.addRequestToItem(itemRequest);
        return itemRepository.save(newItem);
    }

    public Item findById(final Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    final var exceptionMessage = String.format("Item with ID %d does not exist.", itemId);
                    log.error(exceptionMessage);
                    return new NotFoundException(exceptionMessage);
                });
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item updateItem(final long itemId, final ItemRequest itemRequest) {
        itemRepository.findById(itemId).orElseThrow(() -> {
            final var exceptionMessage = String.format("Item with ID %d does not exist.", itemId);
            log.error(exceptionMessage);
            return new NotFoundException(exceptionMessage);
            }
        );
        if (itemRepository.existsByNameAndIdIsNot(itemRequest.name(), itemId)) {
            final var exceptionMessage = String.format("An Item named '%s' already exists", itemRequest.name());
            log.error(exceptionMessage);
            throw new ConflictException(exceptionMessage);
        }

        final Item updatedItem = itemMapper.updateRequestToItem(findById(itemId), itemRequest);
        return itemRepository.save(updatedItem);
    }
}
