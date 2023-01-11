package com.answerdigital.answerking.repository;

import com.answerdigital.answerking.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, CrudRepository<Order, Long> {
    List<Order> findAll();
}
