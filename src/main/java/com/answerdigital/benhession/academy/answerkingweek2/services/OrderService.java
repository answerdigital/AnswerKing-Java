package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.*;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        if (!order.basketHasItem(item)) {
            order.addItemToBasket(item, quantity);
            return orderRepository.save(order);
        }
        throw new ConflictException(String.format("Item id %s is already in the basket", item.getId()));
    }

    public Order updateItemQuantity(Long orderId, Long itemId, Integer itemQuantity) {
        Order order = findById(orderId);
        Item item = itemService.findById(itemId);

        if (order.basketHasItem(item)) {
            order.updateItemInBasket(item, itemQuantity);
            return orderRepository.save(order);
        }
        throw new NotFoundException(String.format("Item id = %s is not in the basket of order id = %s", orderId, itemId));
    }

    public Order deleteItemInBasket(Long orderId, Long itemId) {
        Order order = findById(orderId);
        Item item = itemService.findById(itemId);

        if (order.basketHasItem(item)) {
            order.removeItemFromBasket(item);
            return orderRepository.save(order);
        }
        throw new NotFoundException(String.format("Item id = %s is not in the basket of order id = %s", itemId, orderId));
    }
}