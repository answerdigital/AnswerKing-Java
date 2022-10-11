package com.answerdigital.benhession.academy.answerkingweek2.repositories;

import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

     boolean existsByName(String name);

     boolean existsByNameAndIdIsNot(String name, Integer id);

     List<Item> findAll();
}
