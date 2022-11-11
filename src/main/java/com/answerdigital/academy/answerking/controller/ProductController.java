package com.answerdigital.academy.answerking.controller;

import com.answerdigital.academy.answerking.model.Product;
import com.answerdigital.academy.answerking.request.ProductRequest;
import com.answerdigital.academy.answerking.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        final List<Product> products = productService.findAll();
        return new ResponseEntity<>(products, products.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Product> getProductById(@Valid @PathVariable @NotNull final Long id) {
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody final ProductRequest productRequest, final Errors errors) {
        return new ResponseEntity<>(productService.addNewProduct(productRequest),
                                            errors.hasErrors() ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable @NotNull final long id,
                                              @Valid @RequestBody final ProductRequest productRequest,
                                              final Errors errors) {
        return new ResponseEntity<>(productService.updateProduct(id, productRequest),
                                            errors.hasErrors() ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }
}
