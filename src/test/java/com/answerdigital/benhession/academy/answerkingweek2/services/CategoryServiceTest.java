package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.model.OrderItem;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.CategoryRepository;
import com.answerdigital.benhession.academy.answerkingweek2.request.AddCategoryRequest;
import com.answerdigital.benhession.academy.answerkingweek2.request.UpdateCategoryRequest;

import java.math.BigDecimal;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ItemService itemService;

    Long categoryId = 1L;
    Long itemId = 1L;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(itemService, categoryRepository);
    }

    @Test
    void testAddCategory() {
        // given
        AddCategoryRequest addCategoryRequest = new AddCategoryRequest("Drinks", "Our selection of drinks");
        Category expectedResponse = new Category("Drinks", "Our selection of drinks");

        // when
        doReturn(false).when(categoryRepository).existsByName(anyString());
        doReturn(expectedResponse).when(categoryRepository).save(any());
        Category response = categoryService.addCategory(addCategoryRequest);

        // then
        assertEquals(expectedResponse, response);
        verify(categoryRepository).existsByName(anyString());
        verify(categoryRepository).save(any());
    }

    @Test
    void testAddCategoryThatAlreadyExists() {
        // given
        AddCategoryRequest addCategoryRequest = new AddCategoryRequest("Drinks", "Our selection of drinks");

        // when
        doReturn(true).when(categoryRepository).existsByName(anyString());
        Exception exception = assertThrows(ConflictException.class, () -> categoryService.addCategory(addCategoryRequest));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).existsByName(anyString());
    }

    @Test
    void testUpdateCategory() {
        // given
        Category existingCategory = new Category("Drinks", "Our selection of drinks");
        existingCategory.setId(categoryId);

        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(
                "DrinksUpdated",
                "Our updated selection of drinks");

        Category expectedResponse = new Category("DrinksUpdated", "Our updated selection of drinks");
        expectedResponse.setId(categoryId);

        // when
        doReturn(false).when(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        doReturn(Optional.of(existingCategory)).when(categoryRepository).findById(anyLong());
        doReturn(expectedResponse).when(categoryRepository).save(any());

        Category response = categoryService.updateCategory(updateCategoryRequest, categoryId);

        // then
        assertEquals(expectedResponse, response);
        verify(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        verify(categoryRepository).findById(anyLong());
        verify(categoryRepository).save(any());
    }

    @Test
    void testUpdateCategoryThatDoesNotExist() {
        // given
        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(
                "DrinksUpdated",
                "Our updated selection of drinks");

        // when
        doReturn(false).when(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class, () -> categoryService.updateCategory(updateCategoryRequest, categoryId));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testUpdateCategoryNameToCategoryThatAlreadyExists() {
        // given
        Category existingCategory = new Category("Drinks", "Our selection of drinks");
        existingCategory.setId(categoryId);

        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(
                "Burgers",
                "Our updated selection of drinks");

        // when
        doReturn(true).when(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        Exception exception = assertThrows(ConflictException.class, () -> categoryService.updateCategory(updateCategoryRequest, categoryId));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
    }

    @Test
    void testAddItemToCategory() {
        // given
        Item item = new Item("Coca cola", "This is a coke", BigDecimal.valueOf(2.00d), true);
        item.setId(itemId);

        Category category = new Category("Drinks", "Our selection of drinks");
        category.setId(categoryId);

        Category expectedResponse = new Category("Drinks", "Our selection of drinks");
        expectedResponse.setId(categoryId);
        expectedResponse.addItem(item);

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(item).when(itemService).findById(anyLong());
        doReturn(expectedResponse).when(categoryRepository).save(any());

        Category response = categoryService.addItemToCategory(categoryId, itemId);

        // then
        assertEquals(expectedResponse.getItems(), response.getItems());
        verify(categoryRepository).findById(anyLong());
        verify(itemService).findById(anyLong());
        verify(categoryRepository).save(any());

        // TODO BENCH-57 work out why this isn't returning items... it may be fixed now I added addItem() but we need to double check=====================================================
    }

    @Test
    void testAddItemToCategoryThatIsAlreadyInCategory() {
        // given
        Item item = new Item("Coca cola", "This is a coke", BigDecimal.valueOf(2.00d), true);
        item.setId(itemId);

        Category category = new Category("Drinks", "Our selection of drinks");
        category.setId(categoryId);
        category.addItem(item);

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(item).when(itemService).findById(anyLong());
        Exception exception = assertThrows(ConflictException.class, () -> categoryService.addItemToCategory(categoryId, itemId));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
        verify(itemService).findById(anyLong());
    }

    @Test
    void testAddItemToCategoryThatDoesNotExist() {
        // given
        Item item = new Item("Coca cola", "This is a coke", BigDecimal.valueOf(2.00d), true);
        item.setId(itemId);

        // when
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class, () -> categoryService.addItemToCategory(categoryId, itemId));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testRemoveItemFromCategory() {
        // given
        Item item = new Item("Coca cola", "This is a coke", BigDecimal.valueOf(2.00d), true);
        item.setId(itemId);

        Category category = new Category("Drinks", "Our selection of drinks");
        category.setId(categoryId);
        category.addItem(item);

        Category expectedResponse = new Category("Drinks", "Our selection of drinks");
        expectedResponse.setId(categoryId);

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(item).when(itemService).findById(anyLong());
        doReturn(expectedResponse).when(categoryRepository).save(any());

        Category response = categoryService.removeItemFromCategory(categoryId, itemId);

        // then
        assertEquals(0, response.getItems().size());
    }

    @Test
    void testRemoveItemThatIsNotInCategory() {
        // given
        Item item = new Item("Coca cola", "This is a coke", BigDecimal.valueOf(2.00d), true);
        item.setId(itemId);

        Category category = new Category("Drinks", "Our selection of drinks");
        category.setId(categoryId);

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(item).when(itemService).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class, () -> categoryService.removeItemFromCategory(categoryId, itemId));

        // then
        assertFalse(exception.getMessage().isEmpty());
    }

    @Test
    void testRemoveItemFromCategoryThatDoesNotExist() {
        // given
        Item item = new Item("Coca cola", "This is a coke", BigDecimal.valueOf(2.00d), true);
        item.setId(itemId);

        // when
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class, () -> categoryService.removeItemFromCategory(categoryId, itemId));

        // then
        assertFalse(exception.getMessage().isEmpty());
    }

    @Test
    void testDeleteCategory() {
        // given
        Category category = new Category("Drinks", "Our selection of drinks");
        category.setId(categoryId);

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());

        // then
        assertDoesNotThrow(() -> categoryService.deleteCategoryById(categoryId));
    }

    @Test
    void testDeleteCategoryThatDoesNotExist() {
        // given
        Category category = new Category("Drinks", "Our selection of drinks");
        category.setId(categoryId);

        // when
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class, () -> categoryService.deleteCategoryById(categoryId));

        // then
        assertFalse(exception.getMessage().isEmpty());
    }


    // TODO BENCH-57 ADD VERIFICATION
    //  verify(itemService).findById((Long) any());
    //  etc
}
