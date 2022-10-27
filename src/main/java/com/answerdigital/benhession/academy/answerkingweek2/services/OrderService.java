package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ItemUnavailableException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
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
    public OrderService(final OrderRepository orderRepository, final ItemService itemService) {
        this.orderRepository = orderRepository;
        this.itemService = itemService;
    }

    public Order addOrder(final String address) {
        final Order order = new Order(address);
        return orderRepository.save(order);
    }

    public Order findById(final Long orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("The order with ID %d does not exist.", orderId)));
    }

    public List<Order> findAll() {
        return this.orderRepository.findAll();
    }

    public Order addItemToBasket(final Long orderId, final Long itemId, final Integer quantity) {
       final  Order order = findById(orderId);
        final Item item = itemService.findById(itemId);

        if (!item.getAvailable()) {
            throw new ItemUnavailableException(String.format("The item with ID %d is not available.", item.getId()));
        }

        final Optional<OrderItem> existingOrderItem = order.getOrderItems()
                .stream()
                .filter(orderItem -> orderItem.getItem() == item)
                .findFirst();

        if (existingOrderItem.isPresent()) {
            throw new ConflictException(String.format("Item id %s is already in the basket", item.getId()));
        } else {
            final OrderItem orderItem = new OrderItem(order, item, quantity);
            order.getOrderItems().add(orderItem);
        }

        return orderRepository.save(order);
    }

    public Order updateItemQuantity(final Long orderId, final Long itemId, final Integer itemQuantity) {
        final Order order = findById(orderId);
        final Item item = itemService.findById(itemId);

        final Optional<OrderItem> existingOrderItem = order.getOrderItems()
                .stream()
                .filter(orderItem -> orderItem.getItem() == item)
                .findFirst();

        if (existingOrderItem.isPresent()) {
            existingOrderItem.get().setQuantity(itemQuantity);
        } else {
            throw new NotFoundException(String.format("Item id = %s is not in the basket of order id = %s", orderId, itemId));
        }

        return orderRepository.save(order);
    }

    public Order deleteItemInBasket(final Long orderId, final Long itemId) {
        final Order order = findById(orderId);
        final Item item = itemService.findById(itemId);

        final Optional<OrderItem> existingOrderItem = order.getOrderItems()
                .stream()
                .filter(orderItem -> orderItem.getItem() == item)
                .findFirst();

        if (existingOrderItem.isEmpty()) {
            throw new NotFoundException(String.format("Item id = %s is not in the basket of order id = %s", itemId, orderId));
        }
        order.getOrderItems().remove(existingOrderItem.get());
        return orderRepository.save(order);
    }
}
