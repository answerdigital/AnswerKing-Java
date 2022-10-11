package com.answerdigital.benhession.academy.answerkingweek2.repositories;

import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findAll();
}
