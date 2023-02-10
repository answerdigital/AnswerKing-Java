package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.exception.util.ErrorResponse;
import com.answerdigital.answerking.request.CategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
import com.answerdigital.answerking.response.SimpleProductResponse;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

/**
 * This class is responsible for handling control logic for all incoming requests by
 * exposing a variety of Category related REST endpoints.
 */
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

    /**
     * Exposes an endpoint which allows API users to create new Categories
     * via a POST request. This produces a HTTP status of 201 CREATED if
     * successful, otherwise a status of 400 BAD REQUEST if invalided parameters
     * are provided.
     *
     * @param categoryRequest An instance of {@link com.answerdigital.answerking.request.CategoryRequest}.
     * @return A ResponseEntity of type {@link com.answerdigital.answerking.response.CategoryResponse}.
     */
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

    /**
     * Exposes an endpoint which allows API users to get all existing Categories
     * via a GET request. This produces a HTTP status of 200 OK if there are Categories
     * to be returned. Otherwise, a status of 204 NO CONTENT is produced when no Categories
     * are available.
     *
     * @return A ResponseEntity with a collection of {@link com.answerdigital.answerking.response.CategoryResponse}.
     */
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

    /**
     * Exposes an endpoint which allows API users to get an existing Category via a GET request.
     * This produces a HTTP status of 200 OK if there is a Category available to be returned. Otherwise,
     * a status of 404 NOT FOUND is produced when the Category does not exist.
     *
     * @param categoryId The ID of the Category.
     * @return A ResponseEntity of type {@link com.answerdigital.answerking.response.CategoryResponse}.
     */
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

    /**
     * Exposes an endpoint which allows API users to get a list of all products within a Category via a GET request.
     * This produces a HTTP status of 200 OK if there are or are not products within the Category to be returned.
     * Otherwise, a status of 404 NOT FOUND is produced when the Category does not exist.
     *
     * @param categoryId The ID of the Category.
     * @return A ResponseEntity with a collection of {@link com.answerdigital.answerking.response.ProductResponse}.
     */
    @Operation(summary = "Get all products in a category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When all the products have been returned.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "When the category with the given id does not exist.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<Collection<SimpleProductResponse>> fetchProductsByCategory(
            @PathVariable @NotNull final Long categoryId
    ) {
        return new ResponseEntity<>(categoryService.findProductsByCategoryId(categoryId), HttpStatus.OK);
    }

    /**
     * Exposes an endpoint which allows API users to update a Category via a PUT request. This produces a HTTP status
     * of 200 OK if the update was successful, 400 BAD REQUEST if invalid parameters were provided and 404 NOT FOUND
     * if the Category could not be found.
     *
     * @param categoryRequest An instance of {@link com.answerdigital.answerking.request.CategoryRequest}.
     * @param categoryId The ID of the Category.
     * @return A ResponseEntity of type {@link com.answerdigital.answerking.response.CategoryResponse}.
     */
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

    /**
     * Exposes an endpoint which allows API users to retire a Category via a DELETE request. This produces a HTTP status
     * of 200 OK if the update was successful, 400 BAD REQUEST if invalid parameters were provided, 404 NOT FOUND
     * if the Category could not be found and 410 GONE if the category is already retired.
     *
     * @param categoryId The ID of the Category.
     * @return A ResponseEntity of type {@link java.lang.Void}.
     */
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
    public ResponseEntity<Void> retireCategory(@PathVariable @NotNull final Long categoryId) {
        categoryService.retireCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
