package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.dto.*;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.*;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.services.ItemService;
import com.answerdigital.benhession.academy.answerkingweek2.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

@Valid
@RestController
@RequestMapping(path = "/order")
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;

    @Autowired
    public OrderController(OrderService orderService, ItemService itemService) {
        this.orderService = orderService;
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<Order> addOrder(@RequestBody AddOrderDTO addOrderDTO) {
        return new ResponseEntity<>(this.orderService.addOrder(addOrderDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findById(orderId));
    }

    @GetMapping
    public ResponseEntity<Collection<Order>> getAllOrders() {
        Collection<Order> foundOrders = this.orderService.getAllOrders();
        return new ResponseEntity<>(foundOrders, foundOrders.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/{orderId}/item/{itemId}/quantity/{quantity}")
    public ResponseEntity<Order> addItemToBasket(@PathVariable @NotNull Long orderId,
                                                 @PathVariable @NotNull Long itemId,
                                                 @PathVariable @NotNull Integer quantity) {
        return ResponseEntity.ok(this.orderService.addItemToBasket(orderId, itemId, quantity));
    }

    @DeleteMapping(path = "/{orderId}/item/{itemId}")
    public ResponseEntity<Order> deleteItemInBasket(@PathVariable @NotNull Long orderId,
                                                          @PathVariable @NotNull Long itemId) {
        Optional<Order> orderOptional = orderService.findById(orderId);
        Optional<Item> itemOptional = itemService.findById(itemId);

        if (orderOptional.isPresent() && itemOptional.isPresent()) {
            Order order = orderOptional.get();
            Item item = itemOptional.get();

            if (order.basketHasItem(item)) {
                order.removeItemFromBasket(item);
                Order savedOrder = orderService.update(order);
                GetOrderDTO orderDTO = new GetOrderDTO(savedOrder);

                return new ResponseEntity<>(orderDTO, HttpStatus.OK);

            } else {
                throw new NotFoundException(String.format("Item id = %s is not in the basket of order id = %s",
                        itemId, orderId));
            }

        } else {
            if (orderOptional.isEmpty()) {
                throw new NotFoundException(String.format("Order id = %s not found.", orderId));
            } else {
                throw new NotFoundException(String.format("Item id = %s not found.", itemId));
            }
        }
    }

    @PutMapping(path = "/{orderId}/item/{itemId}/quantity/{quantity}")
    public ResponseEntity<Order> updateItemQuantity(@PathVariable @NotNull Long orderId,
                                                    @PathVariable @NotNull Long itemId,
                                                    @PathVariable @NotNull Integer quantity) {
        return ResponseEntity.ok(this.orderService.updateItemQuantity(orderId, itemId, quantity));
    }
}