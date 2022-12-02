package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.exception.util.ErrorResponse;
import com.answerdigital.answerking.request.CategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
import com.answerdigital.answerking.response.ProductResponse;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

@Validated
@Tag(name = "Inventory", description = "Manage the inventory.")
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
                content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class))}),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
                content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(@Valid @RequestBody final CategoryRequest categoryRequest) {
        return new ResponseEntity<>(categoryService.addCategory(categoryRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all categories.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the list of categories.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class)) })
    })
    @GetMapping
    public ResponseEntity<Collection<CategoryResponse>> getAllCategories() {
        final Set<CategoryResponse> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, categories.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @Operation(summary = "Get a single category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the category with the provided id has been found.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "When the category with the given id does not exist.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable @NotNull final Long categoryId) {
        return new ResponseEntity<>(categoryService.findByIdResponse(categoryId), HttpStatus.OK);
    }

    @Operation(summary = "Get all products in a category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When all the products have been returned.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "When the category with the given id does not exist.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<Collection<ProductResponse>> fetchProductsByCategory(@PathVariable @NotNull final Long categoryId) {
        return new ResponseEntity<>(categoryService.findProductsByCategoryId(categoryId), HttpStatus.OK);
    }

    @Operation(summary = "Add product to a category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Add product to a category.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class)) })
    })
    @PutMapping("/{categoryId}/addproduct/{productId}")
    public ResponseEntity<CategoryResponse> addProductToCategory(@PathVariable @NotNull final Long categoryId,
                                                         @PathVariable @NotNull final Long productId) {
        return new ResponseEntity<>(categoryService.addProductToCategory(categoryId, productId), HttpStatus.OK);
    }

    @Operation(summary = "Remove product from a category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Remove product from a category.",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class)) })
    })
    @PutMapping("/{categoryId}/removeproduct/{productId}")
    public ResponseEntity<CategoryResponse> removeProductFromCategory(@PathVariable @NotNull final Long categoryId,
                                                              @PathVariable @NotNull final Long productId) {
        return new ResponseEntity<>(categoryService.removeProductFromCategory(categoryId, productId), HttpStatus.OK);
    }

    @Operation(summary = "Update an existing category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the category has been updated.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "When the category with the given id does not exist.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @RequestBody final CategoryRequest categoryRequest,
                                                   @PathVariable @NotNull final Long categoryId) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryRequest, categoryId), HttpStatus.OK);
    }

    @Operation(summary = "Retire an existing category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the category has been retired.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "When the category with the given id does not exist.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
        @ApiResponse(responseCode = "410", description = "When the category with the given id is already retired.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> retireCategory(@PathVariable @NotNull final Long categoryId) {
        return new ResponseEntity<>(categoryService.retireCategory(categoryId), HttpStatus.OK);
    }
}
