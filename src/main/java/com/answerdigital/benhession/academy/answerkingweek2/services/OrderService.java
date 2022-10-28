package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ItemUnavailableException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
import com.answerdigital.benhession.academy.answerkingweek2.mappers.OrderMapper;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.model.OrderItem;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.OrderRepository;
import com.answerdigital.benhession.academy.answerkingweek2.request.OrderRequest;
import org.mapstruct.factory.Mappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
                .orElseThrow(() -> {
                    final var exceptionMessage = String.format("The order with ID %d does not exist.", orderId);
                    log.error(exceptionMessage);
                    return new NotFoundException(exceptionMessage);
                });
    }

    public List<Order> findAll() {
        return this.orderRepository.findAll();
    }

    public Order updateOrder(final Long orderId, final OrderRequest orderRequest) {
        final Order orderToUpdate = findById(orderId);
        final Order updatedOrder = orderMapper.updateOrderRequest(orderToUpdate, orderRequest);

        return orderRepository.save(updatedOrder);
    }

    public Order addItemToBasket(final Long orderId, final Long itemId, final Integer quantity) {
        final  Order order = findById(orderId);
        final Item item = itemService.findById(itemId);

        if (!item.isAvailable()) {
            final var exceptionMessage = String.format("The item with ID %d is not available.", item.getId());
            log.error(exceptionMessage);
            throw new ItemUnavailableException(exceptionMessage);
        }

        final Optional<OrderItem> existingOrderItem = order.getOrderItemsSet()
                .stream()
                .filter(orderItem -> orderItem.getItem() == item)
                .findFirst();

        if (existingOrderItem.isPresent()) {
            final var exceptionMessage = String.format("Item id %s is already in the basket", item.getId());
            log.error(exceptionMessage);
            throw new ConflictException(exceptionMessage);
        } else {
            final OrderItem orderItem = new OrderItem(order, item, quantity);
            order.getOrderItemsSet().add(orderItem);
        }

        return orderRepository.save(order);
    }

    public Order updateItemQuantity(final Long orderId, final Long itemId, final Integer itemQuantity) {
        final Order order = findById(orderId);
        final Item item = itemService.findById(itemId);

        final Optional<OrderItem> existingOrderItem = order.getOrderItemsSet()
                .stream()
                .filter(orderItem -> orderItem.getItem() == item)
                .findFirst();

        if (existingOrderItem.isPresent()) {
            existingOrderItem.get().setQuantity(itemQuantity);
        } else {
            final var exceptionMessage = String.format("Item id = %s is not in the basket of order id = %s", orderId, itemId);
            log.error(exceptionMessage);
            throw new NotFoundException(exceptionMessage);
        }

        return orderRepository.save(order);
    }

    public Order deleteItemInBasket(final Long orderId, final Long itemId) {
        final Order order = findById(orderId);
        final Item item = itemService.findById(itemId);

        final Optional<OrderItem> existingOrderItem = order.getOrderItemsSet()
                .stream()
                .filter(orderItem -> orderItem.getItem() == item)
                .findFirst();

        if (existingOrderItem.isEmpty()) {
            final var exceptionMessage = String.format("Item id = %s is not in the basket of order id = %s", itemId, orderId);
            log.error(exceptionMessage);
            throw new NotFoundException(exceptionMessage);
        }
        order.getOrderItemsSet().remove(existingOrderItem.get());
        return orderRepository.save(order);
    }
}
