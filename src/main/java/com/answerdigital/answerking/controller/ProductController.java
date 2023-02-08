package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.exception.util.ErrorResponse;
import com.answerdigital.answerking.request.ProductRequest;
import com.answerdigital.answerking.response.ProductResponse;
import com.answerdigital.answerking.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.util.List;

/**
 * This class is responsible for handling control logic for all incoming requests by
 * exposing a variety of Product related REST endpoints.
 */
@Validated
@Tag(name = "Inventory", description = "Manage the inventory.")
@RestController
@RequestMapping(path = "/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    /**
     * Exposes an endpoint which allows API users to get all existing Products
     * via a GET request. This produces a HTTP status of 200 OK if there are
     * or are not Orders to be returned.
     *
     * @return A ResponseEntity with a list of {@link com.answerdigital.answerking.response.ProductResponse}.
     */
    @Operation(summary = "Get all products.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When all the products have been returned.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)) })
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        final List<ProductResponse> products = productService.findAll();
        return new ResponseEntity<>(products, products.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    /**
     * Exposes an endpoint which allows API users to get an existing Product via a GET request.
     * This produces a HTTP status of 200 OK if there is a Product available to be returned. Otherwise,
     * a status of 404 NOT FOUND is produced when the Product does not exist.
     *
     * @param id The ID of the Product.
     * @return A ResponseEntity of type {@link com.answerdigital.answerking.response.ProductResponse}.
     */
    @Operation(summary = "Get a single product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the product with the provided id has been found.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "When the product with the given id does not exist.",
            content = { @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> getProductById(@Valid @PathVariable @NotNull final Long id) {
        return new ResponseEntity<>(productService.findByIdResponse(id), HttpStatus.OK);
    }

    /**
     * Exposes an endpoint which allows API users to create new Products
     * via a POST request. This produces a HTTP status of 201 CREATED if
     * successful, otherwise a status of 400 BAD REQUEST if invalided parameters
     * are provided.
     *
     * @param productRequest An instance of {@link com.answerdigital.answerking.request.ProductRequest}.
     * @return A ResponseEntity of type {@link com.answerdigital.answerking.response.ProductResponse}.
     */
    @Operation(summary = "Create a new product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "When the product has been created.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
            content = { @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody final ProductRequest productRequest) {
        return new ResponseEntity<>(productService.addNewProduct(productRequest), HttpStatus.CREATED);
    }

    /**
     * Exposes an endpoint which allows API users to update a Product via a PUT request. This produces a HTTP status
     * of 200 OK if the update was successful, 400 BAD REQUEST if invalid parameters were provided and 404 NOT FOUND
     * if the Product could not be found.
     *
     * @param id The ID of the Product.
     * @param productRequest An instance of {@link com.answerdigital.answerking.request.ProductRequest}.
     * @return A ResponseEntity of type {@link com.answerdigital.answerking.response.ProductResponse}.
     */
    @Operation(summary = "Update an existing product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the product has been updated.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
            content = { @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ErrorResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "When the product with the given id does not exist.",
            content = { @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable @NotNull final Long id,
                                              @Valid @RequestBody final ProductRequest productRequest) {
        return new ResponseEntity<>(productService.updateProduct(id, productRequest), HttpStatus.OK);
    }

    /**
     * Exposes an endpoint which allows API users to retire a Product via a DELETE request. This produces a HTTP status
     * of 200 OK if the update was successful, 410 GONE if the Product is already retired and 404 NOT FOUND
     * if the Product could not be found.
     *
     * @param id The ID of the Product.
     * @return A ResponseEntity of type {@link java.lang.Void}.
     */
    @Operation(summary = "Retire an existing product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the product has been retired.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "When the product with the given id does not exist.",
            content = { @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ErrorResponse.class)) }),
        @ApiResponse(responseCode = "410", description = "When the product with the given id is already retired.",
            content = { @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> retireProduct(@PathVariable @NotNull final Long id) {
        productService.retireProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
