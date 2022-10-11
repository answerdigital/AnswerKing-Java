package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.UnableToSaveEntityException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    Logger logger = LoggerFactory.getLogger("Order service");

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private Order save(Order order) throws UnableToSaveEntityException {
        try {
            return orderRepository.save(order);
        } catch (Exception e) {
            logger.error("save order: save operation failed:" + e.getMessage());
            throw new UnableToSaveEntityException("Unable to save order");
        }
    }

    public Optional<Order> addOrder(Order order) throws UnableToSaveEntityException {
        return Optional.of(save(order));
    }

    public Optional<Order> findById(int orderId) {
        return orderRepository.findById(orderId);
    }

    public Order update(Order order) throws UnableToSaveEntityException {
        return save(order);
    }

    public Optional<List<Order>> getAll() {
        List<Order> orders = orderRepository.findAll();

        if(orders.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(orders);
        }
    }
}
