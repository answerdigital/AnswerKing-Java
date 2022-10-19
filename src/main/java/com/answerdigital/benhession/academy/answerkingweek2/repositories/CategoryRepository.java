package com.answerdigital.benhession.academy.answerkingweek2.repositories;

import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>
{
    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, long id);

    Set<Category> findAll();

    int countByIdIn(Set<Long> ids);
}