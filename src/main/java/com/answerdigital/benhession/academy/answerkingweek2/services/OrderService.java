package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.dto.AddOrderDTO;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.*;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.Collection;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    Logger logger = LoggerFactory.getLogger("Order service");

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ItemService itemService) {
        this.orderRepository = orderRepository;
    }

    private Order save(Order order) throws UnableToSaveEntityException {
        try {
            return orderRepository.save(order);
        } catch (PersistenceException exception) {
            logger.error(String.format("The order could not be persisted, reason: %s", exception.getMessage()));
            throw new UnableToSaveEntityException("Unable to save order");
        }
    }

    public Order addOrder(Order order) throws UnableToSaveEntityException {
        return save(order);
    }

    public Order findById(Long orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("The order with ID %d does not exist.", orderId)));
    }

    public Order update(Order order) throws UnableToSaveEntityException {
        return save(order);
    }

    public Collection<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    public Order addItemToBasket(Long orderId, Long itemId, Integer quantity) {
        Order order = getOrderOrThrowNotFound(orderId);
        Item item = getItemOrThrowNotFound(itemId);

        if (!item.isAvailable()) {
            throw new ItemUnavailableException(String.format("The item with ID %d is not available.", item.getId()));
        }

        if (order.basketHasItem((item, quantity)) {
            return orderRepository.save(order);
        }
        throw new ConflictException(String.format("Item id %s is already in the basket", item.getId()));
    }

    public Order updateItemQuantity(Long orderId, Long itemId, Integer itemQuantity) {
        Order order = getOrderOrThrowNotFound(orderId);
        Item item = getItemOrThrowNotFound(itemId);

        if (order.basketHasItem(item)) {
            order.updateItemInBasket(item, itemQuantity);
            return orderRepository.save(order);
        }

        throw new NotFoundException(String.format("Order with ID %d does not contain item in basket with ID %d.", orderId, itemId));
    }

    private Order getOrderOrThrowNotFound(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Order with ID %d does not exist.", orderId)));
    }

    private Item getItemOrThrowNotFound(Long itemId) {
        return itemService.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with ID %d does not exist.", itemId)));
    }
}