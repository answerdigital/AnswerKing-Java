package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import com.answerdigital.benhession.academy.answerkingweek2.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping(path = "/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@Valid @RequestBody Category category) {
        return categoryService.addCategory(category)
                .map(cat -> new ResponseEntity<>(cat, HttpStatus.CREATED))
                .orElseThrow(() -> new ConflictException(String.format("A category named '%s' already exists.",
                        category.getName())));
    }

    @GetMapping
    public ResponseEntity<Collection<Category>> getAllCategories() {
        Set<Category> categories = categoryService.getAll();
        return new ResponseEntity<>(categories, categories.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{categoryId}/additem/{itemId}")
    public ResponseEntity<Category> addItemToCategory(@PathVariable long categoryId, @PathVariable long itemId) {
        return ResponseEntity.ok(categoryService.addItemToCategory(categoryId, itemId));
    }

    @PutMapping("/{categoryId}/removeitem/{itemId}")
    public ResponseEntity<Category> removeItemFromCategory(@PathVariable() long categoryId, @PathVariable long itemId) {
        return ResponseEntity.ok(categoryService.removeItemFromCategory(categoryId, itemId));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable() long categoryId, @Valid @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Category> deleteCategory(long categoryId) {
        return ResponseEntity.of(categoryService.deleteCategoryById(categoryId));
    }
}
