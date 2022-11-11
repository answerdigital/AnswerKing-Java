package com.answerdigital.academy.answerking.service;

import com.answerdigital.academy.answerking.exception.generic.ConflictException;
import com.answerdigital.academy.answerking.exception.generic.NotFoundException;
import com.answerdigital.academy.answerking.model.Category;
import com.answerdigital.academy.answerking.model.Product;
import com.answerdigital.academy.answerking.repository.CategoryRepository;
import com.answerdigital.academy.answerking.request.AddCategoryRequest;
import com.answerdigital.academy.answerking.request.UpdateCategoryRequest;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductService productService;

    private final Long CATEGORY_ID = 1L;
    private final Long PRODUCT_ID = 1L;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(productService, categoryRepository);
    }

    @AfterEach
    void tearDown() {
        categoryService = null;
    }

    @Test
    void testAddCategory() {
        // given
        AddCategoryRequest addCategoryRequest = new AddCategoryRequest("Drinks", "Our selection of drinks");
        Category expectedResponse = new Category("Drinks", "Our selection of drinks");

        // when
        doReturn(false).when(categoryRepository).existsByName(anyString());
        doReturn(expectedResponse).when(categoryRepository).save(any(Category.class));
        Category response = categoryService.addCategory(addCategoryRequest);

        // then
        assertEquals(expectedResponse, response);
        verify(categoryRepository).existsByName(anyString());
        verify(categoryRepository).save(any(Category.class));
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
        existingCategory.setId(CATEGORY_ID);

        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(
                "DrinksUpdated",
                "Our updated selection of drinks");

        Category expectedResponse = new Category("DrinksUpdated", "Our updated selection of drinks");
        expectedResponse.setId(CATEGORY_ID);

        // when
        doReturn(false).when(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        doReturn(Optional.of(existingCategory)).when(categoryRepository).findById(anyLong());
        doReturn(expectedResponse).when(categoryRepository).save(any(Category.class));

        Category response = categoryService.updateCategory(updateCategoryRequest, CATEGORY_ID);

        // then
        assertEquals(expectedResponse, response);
        verify(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        verify(categoryRepository).findById(anyLong());
        verify(categoryRepository).save(any(Category.class));
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
        Exception exception = assertThrows(NotFoundException.class, () -> categoryService.updateCategory(updateCategoryRequest, CATEGORY_ID));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testUpdateCategoryNameToCategoryThatAlreadyExists() {
        // given
        Category existingCategory = new Category("Drinks", "Our selection of drinks");
        existingCategory.setId(CATEGORY_ID);

        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(
                "Burgers",
                "Our updated selection of drinks");

        // when
        doReturn(true).when(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        Exception exception = assertThrows(ConflictException.class, () -> categoryService.updateCategory(updateCategoryRequest, CATEGORY_ID));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
    }

    @Test
    void testAddProductToCategory() {
        // given
        Product product = new Product("Coca cola", "This is a coke", BigDecimal.valueOf(2.00d), true);
        product.setId(PRODUCT_ID);

        Category category = new Category("Drinks", "Our selection of drinks");
        category.setId(CATEGORY_ID);

        Category expectedResponse = new Category("Drinks", "Our selection of drinks");
        expectedResponse.setId(CATEGORY_ID);
        expectedResponse.addProduct(product);

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(product).when(productService).findById(anyLong());
        doReturn(expectedResponse).when(categoryRepository).save(any(Category.class));

        Category response = categoryService.addProductToCategory(CATEGORY_ID, PRODUCT_ID);

        // then
        assertEquals(expectedResponse.getProducts(), response.getProducts());
        verify(categoryRepository).findById(anyLong());
        verify(productService).findById(anyLong());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testAddProductToCategoryThatIsAlreadyInCategory() {
        // given
        Product product = new Product("Coca cola", "This is a coke", BigDecimal.valueOf(2.00d), true);
        product.setId(PRODUCT_ID);

        Category category = new Category("Drinks", "Our selection of drinks");
        category.setId(CATEGORY_ID);
        category.addProduct(product);

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(product).when(productService).findById(anyLong());
        Exception exception = assertThrows(ConflictException.class, () -> categoryService.addProductToCategory(CATEGORY_ID, PRODUCT_ID));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testAddProductToCategoryThatDoesNotExist() {
        // given
        Product product = new Product("Coca cola", "This is a coke", BigDecimal.valueOf(2.00d), true);
        product.setId(PRODUCT_ID);

        // when
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class, () -> categoryService.addProductToCategory(CATEGORY_ID, PRODUCT_ID));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testRemoveProductFromCategory() {
        // given
        Product product = new Product("Coca cola", "This is a coke", BigDecimal.valueOf(2.00d), true);
        product.setId(PRODUCT_ID);

        Category category = new Category("Drinks", "Our selection of drinks");
        category.setId(CATEGORY_ID);
        category.addProduct(product);

        Category expectedResponse = new Category("Drinks", "Our selection of drinks");
        expectedResponse.setId(CATEGORY_ID);

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(product).when(productService).findById(anyLong());
        doReturn(expectedResponse).when(categoryRepository).save(any(Category.class));

        Category response = categoryService.removeProductFromCategory(CATEGORY_ID, PRODUCT_ID);

        // then
        assertEquals(0, response.getProducts().size());
        verify(categoryRepository).findById(anyLong());
        verify(productService).findById(anyLong());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testRemoveProductThatIsNotInCategory() {
        // given
        Product product = new Product("Coca cola", "This is a coke", BigDecimal.valueOf(2.00d), true);
        product.setId(PRODUCT_ID);

        Category category = new Category("Drinks", "Our selection of drinks");
        category.setId(CATEGORY_ID);

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(product).when(productService).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class, () -> categoryService.removeProductFromCategory(CATEGORY_ID, PRODUCT_ID));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testRemoveProductFromCategoryThatDoesNotExist() {
        // given
        Product product = new Product("Coca cola", "This is a coke", BigDecimal.valueOf(2.00d), true);
        product.setId(PRODUCT_ID);

        // when
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class, () -> categoryService.removeProductFromCategory(CATEGORY_ID, PRODUCT_ID));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testDeleteCategory() {
        // given
        Category category = new Category("Drinks", "Our selection of drinks");
        category.setId(CATEGORY_ID);

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());

        // then
        assertDoesNotThrow(() -> categoryService.deleteCategoryById(CATEGORY_ID));
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testDeleteCategoryThatDoesNotExist() {
        // given
        Category category = new Category("Drinks", "Our selection of drinks");
        category.setId(CATEGORY_ID);

        // when
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class, () -> categoryService.deleteCategoryById(CATEGORY_ID));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
    }
}
