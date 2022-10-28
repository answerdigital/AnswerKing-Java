package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.request.OrderRequest;
import com.answerdigital.benhession.academy.answerkingweek2.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
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
@RestController
@RequestMapping(path = "/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
<<<<<<< HEAD
    public ResponseEntity<Order> addOrder(@Valid @RequestBody final OrderRequest orderRequest, final Errors errors) {
        return new ResponseEntity<>(orderService.addOrder(orderRequest),
=======
    public ResponseEntity<Order> addOrder(@Valid @RequestBody final AddOrderRequest addOrderRequest, final Errors errors) {
        return new ResponseEntity<>(orderService.addOrder(addOrderRequest.address()),
>>>>>>> f6fd8e9 (standardizing API code)
                                            errors.hasErrors() ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED);
    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable @NotNull final Long orderId) {
        return new ResponseEntity<>(orderService.findById(orderId), HttpStatus.CREATED);
<<<<<<< HEAD
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable @NotNull final Long orderId,
                                             @Valid @RequestBody final OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.updateOrder(orderId, orderRequest), HttpStatus.OK);
=======
>>>>>>> f6fd8e9 (standardizing API code)
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        final List<Order> foundOrders = orderService.findAll();
        return new ResponseEntity<>(foundOrders, foundOrders.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PostMapping(path = "/{orderId}/item/{itemId}/quantity/{quantity}")
    public ResponseEntity<Order> addItemToBasket(@PathVariable @NotNull final Long orderId,
                                                 @PathVariable @NotNull final Long itemId,
                                                 @PathVariable @NotNull final Integer quantity) {
        return new ResponseEntity<>(orderService.addItemToBasket(orderId, itemId, quantity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{orderId}/item/{itemId}")
    public ResponseEntity<Order> deleteItemInBasket(@PathVariable @NotNull final Long orderId,
                                                    @PathVariable @NotNull final Long itemId) {
        return new ResponseEntity<>(orderService.deleteItemInBasket(orderId, itemId), HttpStatus.OK);
    }

    @PutMapping(path = "/{orderId}/item/{itemId}/quantity/{quantity}")
    public ResponseEntity<Order> updateItemQuantity(@PathVariable @NotNull final Long orderId,
                                                    @PathVariable @NotNull final Long itemId,
                                                    @PathVariable @NotNull final Integer quantity) {
        return new ResponseEntity<>(orderService.updateItemQuantity(orderId, itemId, quantity), HttpStatus.OK);
    }
}
