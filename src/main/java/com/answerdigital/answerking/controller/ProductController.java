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

    @Operation(summary = "Get all products.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When all the products have been returned.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)) })
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        final List<ProductResponse> products = productService.findAll();
        return new ResponseEntity<>(products, products.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @Operation(summary = "Get a single product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When the product with the provided id has been found.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "When the product with the given id does not exist.",
            content = { @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductResponse> getProductById(@Valid @PathVariable @NotNull final Long id) {
        return new ResponseEntity<>(productService.findByIdResponse(id), HttpStatus.OK);
    }

    @Operation(summary = "Create a new product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "When the product has been created.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
            content = { @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody final ProductRequest productRequest) {
        return new ResponseEntity<>(productService.addNewProduct(productRequest), HttpStatus.CREATED);
    }

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
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable @NotNull final Long id,
                                              @Valid @RequestBody final ProductRequest productRequest) {
        return new ResponseEntity<>(productService.updateProduct(id, productRequest), HttpStatus.OK);
    }

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
