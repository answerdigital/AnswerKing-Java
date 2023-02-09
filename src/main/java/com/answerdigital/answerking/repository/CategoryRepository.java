package com.answerdigital.answerking.repository;

import com.answerdigital.answerking.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;

/**
 * The class CategoryRepository is the repository layer for Categories {@link com.answerdigital.answerking.model.Category}.
 * It services requests from
 * CategoryService {@link com.answerdigital.answerking.service.CategoryService}
 * by interacting with the database and performing requested actions.
 */
@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long>, CrudRepository<Category, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Long id);

    Set<Category> findAll();

}
