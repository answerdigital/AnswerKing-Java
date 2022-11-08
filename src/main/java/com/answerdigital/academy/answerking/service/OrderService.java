package com.answerdigital.academy.answerking.service;

import com.answerdigital.academy.answerking.exception.generic.ConflictException;
import com.answerdigital.academy.answerking.exception.custom.ItemUnavailableException;
import com.answerdigital.academy.answerking.exception.generic.NotFoundException;
import com.answerdigital.academy.answerking.mapper.OrderMapper;
import com.answerdigital.academy.answerking.model.Item;
import com.answerdigital.academy.answerking.model.Order;
import com.answerdigital.academy.answerking.model.OrderItem;
import com.answerdigital.academy.answerking.repository.OrderRepository;
import com.answerdigital.academy.answerking.request.OrderRequest;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final ItemService itemService;

    private final OrderMapper orderMapper =
            Mappers.getMapper(OrderMapper.class);

    @Autowired
    public OrderService(final OrderRepository orderRepository, final ItemService itemService) {
        this.orderRepository = orderRepository;
        this.itemService = itemService;
    }

    public Order addOrder(final OrderRequest orderRequest) {
        final Order newOrder = orderMapper.addRequestToOrder(orderRequest);
        return orderRepository.save(newOrder);
    }

    public Order findById(final Long orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("The order with ID %d does not exist.", orderId)));
    }

    @Transactional
    public List<Order> findAll() {
        return this.orderRepository.findAll();
    }

    public Order updateOrder(final Long orderId, final OrderRequest orderRequest) {
        final Order orderToUpdate = findById(orderId);
        final Order updatedOrder = orderMapper.updateOrderRequest(orderToUpdate, orderRequest);

        return orderRepository.save(updatedOrder);
    }

    @Transactional
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
        }

        final OrderItem orderItem = new OrderItem(order, item, quantity);
        order.getOrderItems().add(orderItem);

        return orderRepository.save(order);
    }

    public Order updateItemQuantity(final Long orderId, final Long itemId, final Integer itemQuantity) {
        final Order order = findById(orderId);
        final Item item = itemService.findById(itemId);

        final Optional<OrderItem> existingOrderItem = order.getOrderItems()
                .stream()
                .filter(orderItem -> orderItem.getItem() == item)
                .findFirst();

        if (existingOrderItem.isEmpty()) {
            throw new NotFoundException(String.format("Item id = %s is not in the basket of order id = %s", orderId, itemId));
        }

        existingOrderItem.get().setQuantity(itemQuantity);
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
