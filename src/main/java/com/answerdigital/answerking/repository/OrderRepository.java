package com.answerdigital.answerking.repository;

import com.answerdigital.answerking.model.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
    List<Order> findAll();
}
