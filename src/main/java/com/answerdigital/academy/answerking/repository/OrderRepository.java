package com.answerdigital.academy.answerking.repository;

import com.answerdigital.academy.answerking.model.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
    List<Order> findAll();
}
