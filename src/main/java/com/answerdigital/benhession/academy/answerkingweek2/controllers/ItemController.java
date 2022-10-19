package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
        return new ResponseEntity<>(items, items.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable long id) {
        return ResponseEntity(itemService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Item> addItem(@Valid @RequestBody Item item) {
        return ResponseEntity(itemService.addNewItem(item));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Item> updateItem(@PathVariable() long itemId, @Valid @RequestBody Item item) {
        return ResponseEntity(itemService.updateItem(item, itemId));
    }
}
