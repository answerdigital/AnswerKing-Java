package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.dto.AddItemDTO;
import com.answerdigital.benhession.academy.answerkingweek2.dto.GetItemDTO;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.*;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.services.CategoryService;
import com.answerdigital.benhession.academy.answerkingweek2.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/item")
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;

    @Autowired
    public ItemController(ItemService itemService, CategoryService categoryService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<GetItemDTO>> getAllItems() {
        Optional<List<Item>> itemsOptional = itemService.findAll();

        if (itemsOptional.isPresent()) {
             List<Item> items = itemsOptional.get();
             List<GetItemDTO> itemDTOs = items.stream().map(GetItemDTO::new).toList();

             return new ResponseEntity<>(itemDTOs, HttpStatus.OK);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GetItemDTO> itemById(@PathVariable(name = "id") Integer itemId) {
        Optional<Item> item = itemService.findById(itemId);

        return item
                .map(i -> new ResponseEntity<>(new GetItemDTO(i), HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(String.format("Item with id = %s not found", itemId)));
    }

    @Transactional
    @PostMapping(consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetItemDTO> addItem(@Valid @RequestBody AddItemDTO addItemDTO, BindingResult bindingResult) {

        RestResponseEntityExceptionHandler.checkDTOFields(bindingResult);

        if (addItemDTO.isNotValid()) {
            throw new InvalidValuesException();
        }

        if (!categoryService.allExist(addItemDTO.getCategoryIds())) {
            throw new NotFoundException("One or more of the categories do not exist");
        }

        Optional<Item> savedItem = itemService.addItem(new Item(addItemDTO));

        return saveCategoriesAndGetResponse(addItemDTO, savedItem);
    }

    @Transactional
    @PutMapping(path = "/{id}", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetItemDTO> updateItem(@PathVariable Integer id, @Valid @RequestBody AddItemDTO itemDTO,
                                                 BindingResult bindingResult) {
        Optional<Item> theItem = itemService.findById(id);

        RestResponseEntityExceptionHandler.checkDTOFields(bindingResult);

        if (itemDTO.isNotValid()) {
            throw new InvalidValuesException();
        }

        if (theItem.isPresent()) {
            Item item = theItem.get();
            item.setName(itemDTO.getName());
            item.setDescription(itemDTO.getDescription());
            item.setPrice(new BigDecimal(itemDTO.getPrice()));
            item.setAvailable(itemDTO.isAvailable());

            Optional<Item> savedItem = itemService.updateItem(item);


            return saveCategoriesAndGetResponse(itemDTO, savedItem);

        } else {
            throw new NotFoundException(String.format("Item with id = %s not found", id));
        }

    }

    private ResponseEntity<GetItemDTO> saveCategoriesAndGetResponse(@RequestBody @Valid AddItemDTO itemDTO, Optional<Item> savedItem) {
        Optional<Item> itemWithCategories = savedItem
                .map(i -> itemService.setCategories(i, itemDTO.getCategoryIds()))
                .orElseThrow(() -> new ConflictException(String.format("An Item named '%s' already exists",
                       itemDTO.getName()))
                );

        return itemWithCategories
                .map(i -> new ResponseEntity<>(new GetItemDTO(i), HttpStatus.CREATED))
                .orElseThrow(() -> new UnableToSaveEntityException("Item not saved, unable to add categories"));
    }
}
