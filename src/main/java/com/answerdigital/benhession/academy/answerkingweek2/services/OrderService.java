package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.*;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.model.OrderItem;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemService itemService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ItemService itemService) {
        this.orderRepository = orderRepository;
        this.itemService = itemService;
    }

    public Order addOrder(String address) {
        Order order = new Order(address);
        return orderRepository.save(order);
    }

    public Order findById(Long orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("The order with ID %d does not exist.", orderId)));
    }

    public List<Order> findAll() {
        return this.orderRepository.findAll();
    }

    public Order addItemToBasket(Long orderId, Long itemId, Integer quantity) {
        Order order = findById(orderId);
        Item item = itemService.findById(itemId);

        if (!item.isAvailable()) {
            throw new ItemUnavailableException(String.format("The item with ID %d is not available.", item.getId()));
        }

        Optional<OrderItem> existingOrderItem = order.getOrderItemsSet()
                .stream()
                .filter(orderItem -> orderItem.getItem() == item)
                .findFirst();

        if (existingOrderItem.isPresent()) {
            throw new ConflictException(String.format("Item id %s is already in the basket", item.getId()));
        } else {
            OrderItem orderItem = new OrderItem(order, item, quantity);
            order.getOrderItemsSet().add(orderItem);
        }

        return orderRepository.save(order);
    }

    public Order updateItemQuantity(Long orderId, Long itemId, Integer itemQuantity) {
        Order order = findById(orderId);
        Item item = itemService.findById(itemId);

        Optional<OrderItem> existingOrderItem = order.getOrderItemsSet()
                .stream()
                .filter(orderItem -> orderItem.getItem() == item)
                .findFirst();

        if (existingOrderItem.isPresent()) {
            existingOrderItem.get().setQuantity(itemQuantity);
        } else {
            throw new NotFoundException(String.format("Item id = %s is not in the basket of order id = %s", orderId, itemId));
        }

        orderRepository.save(order);
        return order;
    }

    public Order deleteItemInBasket(Long orderId, Long itemId) {
        Order order = findById(orderId);
        Item item = itemService.findById(itemId);

        Optional<OrderItem> existingOrderItem = order.getOrderItemsSet()
                .stream()
                .filter(orderItem -> orderItem.getItem() == item)
                .findFirst();

        if (existingOrderItem.isEmpty()) {
            throw new NotFoundException(String.format("Item id = %s is not in the basket of order id = %s", itemId, orderId));
        }
        order.getOrderItemsSet().remove(existingOrderItem.get());
        orderRepository.save(order);

        return order;
    }
}