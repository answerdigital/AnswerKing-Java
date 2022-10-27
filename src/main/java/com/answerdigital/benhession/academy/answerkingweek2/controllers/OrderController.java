package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.request.OrderRequest;
import com.answerdigital.benhession.academy.answerkingweek2.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Valid
@RestController
@RequestMapping(path = "/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> addOrder(@RequestBody final OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.addOrder(orderRequest), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable @NotNull final Long orderId) {
        return ResponseEntity.ok(orderService.findById(orderId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable @NotNull final Long orderId,
                                             @RequestBody final OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.updateOrder(orderId, orderRequest), HttpStatus.OK);
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
        return ResponseEntity.ok(orderService.addItemToBasket(orderId, itemId, quantity));
    }

    @DeleteMapping(path = "/{orderId}/item/{itemId}")
    public ResponseEntity<Order> deleteItemInBasket(@PathVariable @NotNull final Long orderId,
                                                    @PathVariable @NotNull final Long itemId) {
        return ResponseEntity.ok(orderService.deleteItemInBasket(orderId, itemId));
    }

    @PutMapping(path = "/{orderId}/item/{itemId}/quantity/{quantity}")
    public ResponseEntity<Order> updateItemQuantity(@PathVariable @NotNull final Long orderId,
                                                    @PathVariable @NotNull final Long itemId,
                                                    @PathVariable @NotNull final Integer quantity) {
        return ResponseEntity.ok(orderService.updateItemQuantity(orderId, itemId, quantity));
    }
}
