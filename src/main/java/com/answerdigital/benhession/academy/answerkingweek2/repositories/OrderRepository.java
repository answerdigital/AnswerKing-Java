package com.answerdigital.benhession.academy.answerkingweek2.repositories;

import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>
{
    List<Order> findAll();
}
