package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.request.ItemRequest;
import com.answerdigital.benhession.academy.answerkingweek2.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Valid
@RestController
@RequestMapping(path = "/item")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.findAll();
        return new ResponseEntity<>(items, items.isEmpty() ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(itemService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Item> addItem(@Valid @RequestBody ItemRequest itemRequest) {
        return ResponseEntity.ok(itemService.addNewItem(itemRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable @NotNull long id, @Valid @RequestBody ItemRequest itemRequest) {
        return ResponseEntity.ok(itemService.updateItem(id, itemRequest));
    }
}
