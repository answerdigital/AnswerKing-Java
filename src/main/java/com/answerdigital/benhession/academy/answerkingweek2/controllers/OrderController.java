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
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
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
    public ResponseEntity<GetOrderDTO> addOrder(@RequestBody @Valid AddOrderDTO addOrderDTO,
                                                BindingResult bindingResult) {

        RestResponseEntityExceptionHandler.checkDTOFields(bindingResult);

        Optional<Order> newOrder = orderService.addOrder(new Order(addOrderDTO));

        return newOrder.map(order -> new ResponseEntity<>(new GetOrderDTO(order), HttpStatus.CREATED))
                .orElseThrow(UnableToSaveEntityException::new);

    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<GetOrderDTO> getOrder(@PathVariable Integer orderId) {
        Optional<Order> orderOptional = orderService.findById(orderId);
        return orderOptional
                .map(order -> new ResponseEntity<>(new GetOrderDTO(order), HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(String.format("Order id = %s not found.", orderId)));
    }

    @GetMapping
    public ResponseEntity<List<GetOrderDTO>> getAllOrders() {
        Optional<List<Order>> ordersOptional = orderService.getAll();

        if (ordersOptional.isPresent()) {
            List<Order> orders = ordersOptional.get();

            List<GetOrderDTO> DTOs = orders.stream()
                    .map(GetOrderDTO::new)
                    .toList();

            return new ResponseEntity<>(DTOs, HttpStatus.OK);

        } else {
           return ResponseEntity.noContent().build();
        }
    }

    @Transactional
    @PostMapping(path = "/{orderId}/item", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetOrderDTO> addItemToBasket(@PathVariable Integer orderId,
                                                       @RequestBody @Valid ItemToBasketDTO itemToBasketDTO,
                                                       BindingResult bindingResult) {
        RestResponseEntityExceptionHandler.checkDTOFields(bindingResult);

        Optional<Order> theOrder = orderService.findById(orderId);
        Optional<Item> theItem = itemService.findById(itemToBasketDTO.getItemId());
        int quantity = itemToBasketDTO.getQuantity();

        if (theOrder.isPresent() && theItem.isPresent()) {
            Order order = theOrder.get();
            Item item = theItem.get();

            if (!item.isAvailable()) {
                throw new ItemUnavailableException(String.format("The item id = %s is currently unavailable",
                        item.getId()));
            }

            if (order.addItemToBasket(item, quantity)) {
                Order savedOrder = orderService.update(order);
                return new ResponseEntity<>(new GetOrderDTO(savedOrder), HttpStatus.CREATED);
            } else {
                throw new ConflictException(String.format("Item id %s is already in the basket", item.getId()));
            }
        } else {
            if (theOrder.isEmpty()) {
                throw new NotFoundException("Order not found.");
            } else {
                throw new NotFoundException("Item not found");
            }
        }
    }

    @Transactional
    @DeleteMapping(path = "/{orderId}/item/{itemId}")
    public ResponseEntity<GetOrderDTO> deleteItemInBasket(@PathVariable Integer orderId,
                                                                    @PathVariable Integer itemId) {
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

    @Transactional
    @PutMapping(path = "/{orderId}/item/{itemId}", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetOrderDTO> updateItemQuantity(@PathVariable Integer orderId,
                                                                    @PathVariable Integer itemId,
                                                                    @Valid @RequestBody ItemQuantityDTO itemQuantityDTO,
                                                                    BindingResult bindingResult) {

        RestResponseEntityExceptionHandler.checkDTOFields(bindingResult);

        Optional<Order> orderOptional = orderService.findById(orderId);
        Optional<Item> itemOptional = itemService.findById(itemId);

        if (orderOptional.isPresent() && itemOptional.isPresent()) {
            Order order = orderOptional.get();
            Item item = itemOptional.get();

            if (order.basketHasItem(item)) {
                order.updateQuantity(item, itemQuantityDTO.getQuantity());
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
}
