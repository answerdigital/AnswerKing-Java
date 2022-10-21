package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.request.AddOrderRequest;
import com.answerdigital.benhession.academy.answerkingweek2.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Valid
@RestController
@RequestMapping(path = "/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> addOrder(@Valid @RequestBody AddOrderRequest addOrderRequest) {
        return new ResponseEntity<>(orderService.addOrder(addOrderRequest.address()), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable @NotNull Long orderId) {
        return ResponseEntity.ok(orderService.findById(orderId));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> foundOrders = orderService.findAll();
        return new ResponseEntity<>(foundOrders, foundOrders.isEmpty() ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/{orderId}/item/{itemId}/quantity/{quantity}")
    public ResponseEntity<Order> addItemToBasket(@PathVariable @NotNull Long orderId,
                                                 @PathVariable @NotNull Long itemId,
                                                 @PathVariable @NotNull Integer quantity) {
        return ResponseEntity.ok(orderService.addItemToBasket(orderId, itemId, quantity));
    }

    @DeleteMapping(path = "/{orderId}/item/{itemId}")
    public ResponseEntity<Order> deleteItemInBasket(@PathVariable @NotNull Long orderId,
                                                    @PathVariable @NotNull Long itemId) {
        return ResponseEntity.ok(orderService.deleteItemInBasket(orderId, itemId));
    }

    @PutMapping(path = "/{orderId}/item/{itemId}/quantity/{quantity}")
    public ResponseEntity<Order> updateItemQuantity(@PathVariable @NotNull Long orderId,
                                                    @PathVariable @NotNull Long itemId,
                                                    @PathVariable @NotNull Integer quantity) {
        return ResponseEntity.ok(orderService.updateItemQuantity(orderId, itemId, quantity));
    }
}