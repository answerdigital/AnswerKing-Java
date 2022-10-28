package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import com.answerdigital.benhession.academy.answerkingweek2.request.AddCategoryRequest;
import com.answerdigital.benhession.academy.answerkingweek2.request.UpdateCategoryRequest;
import com.answerdigital.benhession.academy.answerkingweek2.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

@Validated
@RestController
@RequestMapping(path = "/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@Valid @RequestBody final AddCategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.addCategory(categoryRequest));
    }

    @GetMapping
    public ResponseEntity<Collection<Category>> getAllCategories() {
        final Set<Category> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, categories.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PutMapping("/{categoryId}/additem/{itemId}")
    public ResponseEntity<Category> addItemToCategory(@PathVariable @NotNull final Long categoryId,
                                                      @PathVariable @NotNull final Long itemId) {
        return ResponseEntity.ok(categoryService.addItemToCategory(categoryId, itemId));
    }

    @PutMapping("/{categoryId}/removeitem/{itemId}")
    public ResponseEntity<Category> removeItemFromCategory(@PathVariable @NotNull final Long categoryId,
                                                           @PathVariable @NotNull final Long itemId) {
        return ResponseEntity.ok(categoryService.removeItemFromCategory(categoryId, itemId));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody final UpdateCategoryRequest updateCategoryRequest,
                                                   @PathVariable @NotNull final Long categoryId) {
        return ResponseEntity.ok(categoryService.updateCategory(updateCategoryRequest, categoryId));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable @NotNull final Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
