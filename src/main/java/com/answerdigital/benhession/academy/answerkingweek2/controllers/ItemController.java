package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.request.ItemRequest;
import com.answerdigital.benhession.academy.answerkingweek2.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(final ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        final List<Item> items = itemService.findAll();
        return new ResponseEntity<>(items, items.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Item> getItemById(@Valid @PathVariable @NotNull final Long id) {
        return new ResponseEntity<>(itemService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Item> addItem(@Valid @RequestBody final ItemRequest itemRequest, final Errors errors) {
        return new ResponseEntity<>(itemService.addNewItem(itemRequest),
                                            errors.hasErrors() ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable @NotNull final long id,
                                           @Valid @RequestBody final ItemRequest itemRequest,
                                           final Errors errors) {
        return new ResponseEntity<>(itemService.updateItem(id, itemRequest),
                                            errors.hasErrors() ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }
}
