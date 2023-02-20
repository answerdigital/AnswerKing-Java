package com.answerdigital.answerking.repository;

import com.answerdigital.answerking.model.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, Long>, CrudRepository<Tag, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Long id);

    Set<Tag> findAll();
}
