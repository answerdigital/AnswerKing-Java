package com.answerdigital.benhession.academy.answerkingweek2.repositories;

import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Long id);

    List<Item> findAll();
}
