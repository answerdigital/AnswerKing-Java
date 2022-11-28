package com.answerdigital.answerking.service;

import com.answerdigital.answerking.builder.AddCategoryRequestTestBuilder;
import com.answerdigital.answerking.builder.CategoryTestBuilder;
import com.answerdigital.answerking.builder.ProductTestBuilder;
import com.answerdigital.answerking.builder.UpdateCategoryRequestTestBuilder;
import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.custom.ProductAlreadyPresentException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.repository.CategoryRepository;
import com.answerdigital.answerking.request.AddCategoryRequest;
import com.answerdigital.answerking.request.UpdateCategoryRequest;

import com.answerdigital.answerking.response.CategoryResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private final CategoryTestBuilder categoryTestBuilder;

    private final AddCategoryRequestTestBuilder addCategoryRequestTestBuilder;

    private final UpdateCategoryRequestTestBuilder updateCategoryRequestTestBuilder;

    private final ProductTestBuilder productTestBuilder;

    private static final Long NONEXISTENT_CATEGORY_ID = 2L;

    private CategoryServiceTest() {
        categoryTestBuilder = new CategoryTestBuilder();
        addCategoryRequestTestBuilder = new AddCategoryRequestTestBuilder();
        updateCategoryRequestTestBuilder = new UpdateCategoryRequestTestBuilder();
        productTestBuilder = new ProductTestBuilder();
    }

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
        final AddCategoryRequest addCategoryRequest = addCategoryRequestTestBuilder.withDefaultValues().build();
        final Category expectedResponse = categoryTestBuilder.withDefaultValues().build();

        // when
        doReturn(false).when(categoryRepository).existsByName(anyString());
        doReturn(expectedResponse).when(categoryRepository).save(any(Category.class));
        final CategoryResponse categoryResponse = categoryService.addCategory(addCategoryRequest);

        // then
        assertAll(
                () -> assertEquals(expectedResponse.getId(), categoryResponse.getId()),
                () -> assertEquals(expectedResponse.getName(), categoryResponse.getName()),
                () -> assertEquals(expectedResponse.getDescription(), categoryResponse.getDescription())
        );
        verify(categoryRepository).existsByName(anyString());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testAddCategoryThatAlreadyExists() {
        // given
        final AddCategoryRequest addCategoryRequest = addCategoryRequestTestBuilder.withDefaultValues().build();

        // when
        doReturn(true).when(categoryRepository).existsByName(anyString());
        Exception exception = assertThrows(NameUnavailableException.class,
                () -> categoryService.addCategory(addCategoryRequest));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).existsByName(anyString());
    }

    @Test
    void testUpdateCategory() {
        // given
        final Category existingCategory = categoryTestBuilder.withDefaultValues().build();
        final UpdateCategoryRequest updateCategoryRequest = updateCategoryRequestTestBuilder.withDefaultValues().build();
        final Category expectedResponse = categoryTestBuilder
                .withDefaultValues()
                .withName(updateCategoryRequest.name())
                .withDescription(updateCategoryRequest.description())
                .build();

        // when
        doReturn(false).when(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        doReturn(Optional.of(existingCategory)).when(categoryRepository).findById(anyLong());
        doReturn(expectedResponse).when(categoryRepository).save(any(Category.class));

        Category response = categoryService.updateCategory(updateCategoryRequest, existingCategory.getId());

        // then
        assertEquals(expectedResponse, response);
        verify(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        verify(categoryRepository).findById(anyLong());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testUpdateCategoryThatDoesNotExist() {
        // given
        final UpdateCategoryRequest updateCategoryRequest = updateCategoryRequestTestBuilder.withDefaultValues().build();

        // when
        doReturn(false).when(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class,
                () -> categoryService.updateCategory(updateCategoryRequest, NONEXISTENT_CATEGORY_ID));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testUpdateCategoryNameToCategoryThatAlreadyExists() {
        // given
        final Category existingCategory = categoryTestBuilder.withDefaultValues().build();
        final UpdateCategoryRequest updateCategoryRequest = updateCategoryRequestTestBuilder.withDefaultValues().build();

        // when
        doReturn(true).when(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        Exception exception = assertThrows(NameUnavailableException.class,
                () -> categoryService.updateCategory(updateCategoryRequest, existingCategory.getId()));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
    }

    @Test
    void testAddProductToCategory() {
        // given
        final Product product = productTestBuilder.withDefaultValues().build();
        final Category category = categoryTestBuilder.withDefaultValues().build();
        final Category expectedResponse = categoryTestBuilder
                .withDefaultValues()
                .withProduct(product)
                .build();

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(product).when(productService).findById(anyLong());
        doReturn(expectedResponse).when(categoryRepository).save(any(Category.class));

        Category response = categoryService.addProductToCategory(category.getId(), product.getId());

        // then
        assertEquals(expectedResponse.getProducts(), response.getProducts());
        verify(categoryRepository).findById(anyLong());
        verify(productService).findById(anyLong());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testAddProductToCategoryThatIsAlreadyInCategory() {
        // given
        final Product product = productTestBuilder.withDefaultValues().build();
        final Category category = categoryTestBuilder
                .withDefaultValues()
                .withProduct(product)
                .build();

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(product).when(productService).findById(anyLong());
        Exception exception = assertThrows(ProductAlreadyPresentException.class,
                () -> categoryService.addProductToCategory(category.getId(), product.getId()));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testAddProductToCategoryThatDoesNotExist() {
        // given
        final Product product = productTestBuilder.withDefaultValues().build();

        // when
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class,
                () -> categoryService.addProductToCategory(NONEXISTENT_CATEGORY_ID, product.getId()));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testRemoveProductFromCategory() {
        // given
        final Product product = productTestBuilder.withDefaultValues().build();
        final Category category = categoryTestBuilder
                .withDefaultValues()
                .withProduct(product)
                .build();
        final Category expectedResponse = categoryTestBuilder.withDefaultValues().build();

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(product).when(productService).findById(anyLong());
        doReturn(expectedResponse).when(categoryRepository).save(any(Category.class));

        Category response = categoryService.removeProductFromCategory(category.getId(), product.getId());

        // then
        assertEquals(0, response.getProducts().size());
        verify(categoryRepository).findById(anyLong());
        verify(productService).findById(anyLong());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testRemoveProductThatIsNotInCategory() {
        // given
        final Product product = productTestBuilder.withDefaultValues().build();
        final Category category = categoryTestBuilder.withDefaultValues().build();

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(product).when(productService).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class,
                () -> categoryService.removeProductFromCategory(category.getId(), product.getId()));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testRemoveProductFromCategoryThatDoesNotExist() {
        // given
        Product product = productTestBuilder.withDefaultValues().build();

        // when
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());
        Exception exception = assertThrows(NotFoundException.class,
                () -> categoryService.removeProductFromCategory(NONEXISTENT_CATEGORY_ID, product.getId()));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testRetireCategory() {
        // given
        Category category = categoryTestBuilder.withDefaultValues().build();
        Category expectedCategory = categoryTestBuilder
                .withDefaultValues()
                .withRetired(true)
                .build();

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(expectedCategory).when(categoryRepository).save(any(Category.class));

        // then
        assertEquals(expectedCategory, categoryService.retireCategory(category.getId()));
        verify(categoryRepository).findById(anyLong());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testRetireCategoryAlreadyRetiredThrowsRetirementException() {
        // given
        Category retiredCategory = categoryTestBuilder
                .withDefaultValues()
                .withRetired(true)
                .build();

        // when
        doReturn(Optional.of(retiredCategory)).when(categoryRepository).findById(anyLong());

        // then
        assertThrows(RetirementException.class, () -> categoryService.retireCategory(retiredCategory.getId()));
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testRetireCategoryDoesNotExistThrowsNotFoundException() {
        // when
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());

        // then
        assertThrows(NotFoundException.class, () -> categoryService.retireCategory(NONEXISTENT_CATEGORY_ID));
        verify(categoryRepository).findById(anyLong());
    }
}
