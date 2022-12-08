package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.request.OrderRequest;
import com.answerdigital.answerking.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Tag(name = "Orders", description = "Create and manage customer orders.")
@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> addOrder(@Valid @RequestBody final OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.addOrder(orderRequest), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable @NotNull final Long orderId) {
        return new ResponseEntity<>(orderService.findById(orderId), HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable @NotNull final Long orderId,
                                             @Valid @RequestBody final OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.updateOrder(orderId, orderRequest), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        final List<Order> foundOrders = orderService.findAll();
        return new ResponseEntity<>(foundOrders, foundOrders.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PostMapping(path = "/{orderId}/product/{productId}/quantity/{quantity}")
    public ResponseEntity<Order> addProductToBasket(@PathVariable @NotNull final Long orderId,
                                                    @PathVariable @NotNull final Long productId,
                                                    @PathVariable @NotNull final Integer quantity) {
        return new ResponseEntity<>(orderService.addProductToBasket(orderId, productId, quantity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{orderId}/product/{productId}")
    public ResponseEntity<Void> deleteProductInBasket(@PathVariable @NotNull final Long orderId,
                                                       @PathVariable @NotNull final Long productId) {
        orderService.deleteProductInBasket(orderId, productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{orderId}/product/{productId}/quantity/{quantity}")
    public ResponseEntity<Order> updateProductQuantity(@PathVariable @NotNull final Long orderId,
                                                       @PathVariable @NotNull final Long productId,
                                                       @PathVariable @NotNull final Integer quantity) {
        return new ResponseEntity<>(orderService.updateProductQuantity(orderId, productId, quantity), HttpStatus.OK);
    }
}
