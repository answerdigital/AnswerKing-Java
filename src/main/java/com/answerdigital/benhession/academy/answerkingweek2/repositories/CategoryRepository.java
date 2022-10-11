package com.answerdigital.benhession.academy.answerkingweek2.repositories;

import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Integer id);

    Set<Category> findAll();

    int countByIdIn(Set<Integer> ids);

}
