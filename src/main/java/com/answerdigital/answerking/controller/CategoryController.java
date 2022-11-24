package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.exception.util.ErrorResponse;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.request.AddCategoryRequest;
import com.answerdigital.answerking.request.UpdateCategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
import com.answerdigital.answerking.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Categories", description = "The Categories API")
@RestController
@RequestMapping(path = "/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create a new category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "When the category has been created.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) }),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(@Valid @RequestBody final AddCategoryRequest categoryRequest,
                                                final Errors errors) {
        return new ResponseEntity<>(categoryService.addCategory(categoryRequest),
                                                        errors.hasErrors() ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED);
    }

    @Operation(summary = "Get all categories.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the list of categories.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) })
    })
    @GetMapping
    public ResponseEntity<Collection<CategoryResponse>> getAllCategories() {
        final Set<CategoryResponse> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, categories.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @Operation(summary = "Get a single category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the category with the provided id has been found.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) }),
        @ApiResponse(responseCode = "404", description = "When the category with the given id does not exist.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable @NotNull final Long categoryId) {
        return new ResponseEntity<>(categoryService.findById(categoryId), HttpStatus.OK);
    }

    @Operation(summary = "Add product to a category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Add product to a category.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) })
    })
    @PutMapping("/{categoryId}/addproduct/{productId}")
    public ResponseEntity<Category> addProductToCategory(@PathVariable @NotNull final Long categoryId,
                                                         @PathVariable @NotNull final Long productId) {
        return new ResponseEntity<>(categoryService.addProductToCategory(categoryId, productId), HttpStatus.OK);
    }

    @Operation(summary = "Remove product from a category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Remove product from a category.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) })
    })
    @PutMapping("/{categoryId}/removeproduct/{productId}")
    public ResponseEntity<Category> removeProductFromCategory(@PathVariable @NotNull final Long categoryId,
                                                              @PathVariable @NotNull final Long productId) {
        return new ResponseEntity<>(categoryService.removeProductFromCategory(categoryId, productId), HttpStatus.OK);
    }

    @Operation(summary = "Update an existing category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the category has been updated.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) }),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "When the category with the given id does not exist.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody final UpdateCategoryRequest updateCategoryRequest,
                                                   @PathVariable @NotNull final Long categoryId,
                                                   final Errors errors) {
        return new ResponseEntity<>(categoryService.updateCategory(updateCategoryRequest, categoryId),
                                                    errors.hasErrors() ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @Operation(summary = "Retire an existing category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the category has been retired.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)) }),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "When the category with the given id does not exist.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
        @ApiResponse(responseCode = "410", description = "When the category with the given id is already retired.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Category> retireCategory(@PathVariable @NotNull final Long categoryId) {
        return new ResponseEntity<>(categoryService.retireCategory(categoryId), HttpStatus.OK);
    }
}
