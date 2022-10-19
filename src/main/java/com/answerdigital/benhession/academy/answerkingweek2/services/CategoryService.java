package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.UnableToSaveEntityException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.CategoryRepository;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    CategoryRepository categoryRepository;
    ItemRepository itemRepository;
    Logger logger = LoggerFactory.getLogger("Category service");

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    public Optional<Category> addCategory(Category category) throws UnableToSaveEntityException {
        if (categoryRepository.existsByName(category.getName())) {
            return Optional.empty();
        } else {
            return Optional.of(save(category));
        }
    }

    public Category updateCategory(Category category, long categoryId) {
        Optional<Category> catById = categoryRepository.findById(categoryId);
        if (catById.isEmpty())
            throw new NotFoundException("Category with id " + categoryId + " does not exist");

        if (categoryRepository.existsByNameAndIdIsNot(category.getName(), categoryId))
            throw new ConflictException(String.format("A category named %s already exists", category.getName()));

        if ((category.getItemsSet() == null || category.getItemsSet().isEmpty()) && !catById.get().getItemsSet().isEmpty())
            category.getItemsSet().addAll(catById.get().getItemsSet());

        return categoryRepository.save(category);
    }

    private Category save(Category category) throws UnableToSaveEntityException {
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            logger.error("save category: save operation failed");
            throw new UnableToSaveEntityException(String.format("Unable to save category = %s", category.getName()));
        }
    }

    public Set<Category> getAll() {
        return categoryRepository.findAll();
    }

    public boolean allExist(Set<Long> ids) {
        return ids.size() == categoryRepository.countByIdIn(ids);
    }

    public Category addItemToCategory(long categoryId, long itemId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new NotFoundException("Category does not exist}");
        }

        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new NotFoundException("Item does not exist}");
        }

        category.get().getItemsSet().add(item.get());
        return categoryRepository.save(category.get());
    }

    public Category removeItemFromCategory(Long categoryId, Long itemId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new NotFoundException("Category does not exist");
        }

        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new NotFoundException("Item does not exist");
        }

        category.get().getItemsSet().remove(item.get());
        categoryRepository.save(category.get());

        return category;
    }

    public Optional<Category> deleteCategoryById(long id) {
        Optional<Category> deletedCategory = categoryRepository.findById(id);
        if (deletedCategory.isEmpty()) {
            throw new NotFoundException("Category does not exist");
        }

        categoryRepository.deleteById(id);
        return deletedCategory;
    }

}
