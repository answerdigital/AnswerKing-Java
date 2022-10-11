package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.dto.AddCategoryDTO;
import com.answerdigital.benhession.academy.answerkingweek2.dto.GetCategoryDTO;
import com.answerdigital.benhession.academy.answerkingweek2.dto.ItemToCategoryDTO;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.*;
import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path ="/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final ItemService itemService;


    @Autowired
    public CategoryController(CategoryService categoryService, ItemService itemService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
    }

    @PostMapping(consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetCategoryDTO> addCategory(@Valid @RequestBody AddCategoryDTO addCategoryDTO,
                                                      BindingResult bindingResult) {

        RestResponseEntityExceptionHandler.checkDTOFields(bindingResult);

        if (addCategoryDTO.isNotValid()) {
            throw new InvalidValuesException("The fields entered for the category contain errors");
        }

        Optional<Category> savedCategory = categoryService.addCategory(new Category(addCategoryDTO));

        return savedCategory
                .map(category -> new ResponseEntity<>(new GetCategoryDTO(category), HttpStatus.CREATED))
                .orElseThrow(() -> new ConflictException(String.format("A category named '%s' already exists.",
                        addCategoryDTO.getName())));
    }

    @GetMapping
    public ResponseEntity<Set<GetCategoryDTO>> getAllCategories() {
        Optional<Set<Category>> currentCategories = categoryService.getAll();

        return currentCategories
                .map(categories -> {
                    Set<GetCategoryDTO> dtoSet = categories.stream()
                            .map(GetCategoryDTO::new)
                            .collect(Collectors.toSet());
                    return new ResponseEntity<>(dtoSet, HttpStatus.OK);

                })
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Transactional
    @PostMapping(path = "/{categoryId}/item", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetCategoryDTO> addItemById(@PathVariable Integer categoryId,
                                                      @Valid @RequestBody ItemToCategoryDTO itemDTO,
                                                      BindingResult bindingResult) {

        RestResponseEntityExceptionHandler.checkDTOFields(bindingResult);

        Optional<Category> theCategory = categoryService.findById(categoryId);
        Optional<Item> theItem = itemService.findById(itemDTO.getItemId());

        if (theCategory.isPresent() && theItem.isPresent()) {
            Category category = theCategory.get();
            Item item = theItem.get();

            if (category.containsItem(item)) {
                throw new ConflictException(String.format("%s is already added to the category %s",
                        item.getName(), category.getName()));
            } else {

                GetCategoryDTO dto = new GetCategoryDTO(itemService.addCategory(item, category));
                return new ResponseEntity<>(dto, HttpStatus.OK);

            }

        } else {
            if (theCategory.isEmpty()) {
                throw new NotFoundException(String.format("Category with id = %s not found.", categoryId));
            } else {
                throw new NotFoundException(String.format("Item with id = %s not found.", itemDTO.getItemId()));
            }
        }
    }

    @Transactional
    @DeleteMapping(path = "/{categoryId}/item/{itemId}")
    public ResponseEntity<GetCategoryDTO> removeItemById(@PathVariable Integer categoryId,
                                                         @PathVariable Integer itemId) {

        Optional<Category> theCategory = categoryService.findById(categoryId);
        Optional<Item> theItem = itemService.findById(itemId);

        if (theCategory.isPresent() && theItem.isPresent()) {
            Item item = theItem.get();
            Category category = theCategory.get();

            if(category.containsItem(item)) {
                GetCategoryDTO updatedCategoryDTO = new GetCategoryDTO(itemService.removeCategory(item, category));
                return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);

            } else {
                throw new NotFoundException(String.format("Item %s is not in the category %s",
                        item.getName(), category.getName()));
            }

        } else {
            if (theCategory.isEmpty()) {
                throw new NotFoundException(String.format("Category with id = %s not found.", categoryId));
            } else {
                throw new NotFoundException(String.format("Item with id = %s not found.", itemId));
            }
        }
    }

    @Transactional
    @PutMapping(path = "/{categoryId}", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetCategoryDTO> updateCategory(@PathVariable Integer categoryId,
                                                         @Valid @RequestBody AddCategoryDTO categoryDTO,
                                                         BindingResult bindingResult) {

        RestResponseEntityExceptionHandler.checkDTOFields(bindingResult);

        if (categoryDTO.isNotValid()) {
           throw new InvalidValuesException("The fields entered for the category contain errors.");
        }

        Optional<Category> theCategory = categoryService.findById(categoryId);

        if (theCategory.isPresent()) {
            Category category = theCategory.get();
            category.setName(categoryDTO.getName());
            category.setDescription(categoryDTO.getDescription());

            Optional<Category> savedCategory = categoryService.updateCategory(category);

            return savedCategory.map(i -> (new ResponseEntity<>(new GetCategoryDTO(i), HttpStatus.OK)))
                    .orElseThrow(() -> new ConflictException(
                            String.format("A category named %s already exists", category.getName())));

        } else {
            throw new NotFoundException(String.format("Category id = %s not found.", categoryId));
        }
    }

    @Transactional
    @DeleteMapping("{categoryId}")
    public ResponseEntity<GetCategoryDTO> deleteCategory(@PathVariable int categoryId) {

        Optional<Category> theCategory = categoryService.findById(categoryId);

        return theCategory
                .map(category -> {
                    Category deletedCategory = categoryService.remove(category);
                    return new ResponseEntity<>(new GetCategoryDTO(deletedCategory), HttpStatus.OK);
                })
                .orElseThrow(() -> new NotFoundException(
                        String.format("Category id = %s not found", categoryId)));
    }
}
