package com.answerdigital.answerking.repository;

import com.answerdigital.answerking.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The class OrderRepository is the repository layer for Orders {@link com.answerdigital.answerking.model.Order}.
 * It services requests from
 * OrderService {@link com.answerdigital.answerking.service.OrderService}
 * by interacting with the database and performing requested actions.
 */
@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, CrudRepository<Order, Long> {
    List<Order> findAll();
}
