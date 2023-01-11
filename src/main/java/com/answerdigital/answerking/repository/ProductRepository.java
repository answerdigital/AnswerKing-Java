package com.answerdigital.answerking.repository;

import com.answerdigital.answerking.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, CrudRepository<Product, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(final String name, final Long id);

    List<Product> findAll();

    List<Product> findProductsByCategoryId(Long categoryId);

    List<Product> findAllByIdIn(final List<Long> productIds);
}
