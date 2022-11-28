package com.answerdigital.answerking.repository;

import com.answerdigital.answerking.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Long id);

    List<Product> findProductsByCategoryId(Long categoryId);

    List<Product> findAll();

    List<Product> findProductsByCategoriesId(Long categoryId);
}
