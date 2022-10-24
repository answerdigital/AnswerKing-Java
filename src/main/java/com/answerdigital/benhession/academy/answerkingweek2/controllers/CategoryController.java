package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import com.answerdigital.benhession.academy.answerkingweek2.request.AddCategoryRequest;
import com.answerdigital.benhession.academy.answerkingweek2.request.UpdateCategoryRequest;
import com.answerdigital.benhession.academy.answerkingweek2.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

@Valid
@RestController
@RequestMapping(path = "/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody AddCategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.addCategory(categoryRequest));
    }

    @GetMapping
    public ResponseEntity<Collection<Category>> getAllCategories() {
        Set<Category> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, categories.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PutMapping("/{categoryId}/additem/{itemId}")
    public ResponseEntity<Category> addItemToCategory(@PathVariable @NotNull Long categoryId,
                                                      @PathVariable @NotNull Long itemId) {
        return ResponseEntity.ok(categoryService.addItemToCategory(categoryId, itemId));
    }

    @PutMapping("/{categoryId}/removeitem/{itemId}")
    public ResponseEntity<Category> removeItemFromCategory(@PathVariable @NotNull Long categoryId,
                                                           @PathVariable @NotNull Long itemId) {
        return ResponseEntity.ok(categoryService.removeItemFromCategory(categoryId, itemId));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@RequestBody UpdateCategoryRequest updateCategoryRequest,
                                                   @PathVariable @NotNull Long categoryId) {
        return ResponseEntity.ok(categoryService.updateCategory(updateCategoryRequest, categoryId));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable @NotNull Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
