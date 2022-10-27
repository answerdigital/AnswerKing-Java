package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.request.AddOrderRequest;
import com.answerdigital.benhession.academy.answerkingweek2.services.OrderService;
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
@RestController
@RequestMapping(path = "/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> addOrder(@Valid @RequestBody final AddOrderRequest addOrderRequest) {
        return new ResponseEntity<>(orderService.addOrder(addOrderRequest.address()), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable @NotNull final Long orderId) {
        return ResponseEntity.ok(orderService.findById(orderId));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        final List<Order> foundOrders = orderService.findAll();
        return new ResponseEntity<>(foundOrders, foundOrders.isEmpty() ? HttpStatus.OK : HttpStatus.NO_CONTENT);
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
