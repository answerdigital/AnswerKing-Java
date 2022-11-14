package com.answerdigital.academy.answerking.repository;

import com.answerdigital.academy.answerking.model.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
    List<Order> findAll();
}
