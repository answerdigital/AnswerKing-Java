package com.answerdigital.academy.answerking.controller;

import com.answerdigital.academy.answerking.model.Category;
import com.answerdigital.academy.answerking.request.AddCategoryRequest;
import com.answerdigital.academy.answerking.request.UpdateCategoryRequest;
import com.answerdigital.academy.answerking.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
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
@RequestMapping(path = "/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@Valid @RequestBody final AddCategoryRequest categoryRequest,
                                                final Errors errors) {
        return new ResponseEntity<>(categoryService.addCategory(categoryRequest),
                                                        errors.hasErrors() ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<Category>> getAllCategories() {
        final Set<Category> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, categories.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PutMapping("/{categoryId}/addproduct/{productId}")
    public ResponseEntity<Category> addProductToCategory(@PathVariable @NotNull final Long categoryId,
                                                         @PathVariable @NotNull final Long productId) {
        return new ResponseEntity<>(categoryService.addProductToCategory(categoryId, productId), HttpStatus.OK);
    }

    @PutMapping("/{categoryId}/removeproduct/{productId}")
    public ResponseEntity<Category> removeProductFromCategory(@PathVariable @NotNull final Long categoryId,
                                                              @PathVariable @NotNull final Long productId) {
        return new ResponseEntity<>(categoryService.removeProductFromCategory(categoryId, productId), HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody final UpdateCategoryRequest updateCategoryRequest,
                                                   @PathVariable @NotNull final Long categoryId,
                                                   final Errors errors) {
        return new ResponseEntity<>(categoryService.updateCategory(updateCategoryRequest, categoryId),
                                                    errors.hasErrors() ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable @NotNull final Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
