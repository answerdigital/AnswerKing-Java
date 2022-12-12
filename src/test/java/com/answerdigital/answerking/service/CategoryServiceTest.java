package com.answerdigital.answerking.service;

import com.answerdigital.answerking.builder.CategoryRequestTestBuilder;
import com.answerdigital.answerking.builder.CategoryTestBuilder;
import com.answerdigital.answerking.builder.ProductTestBuilder;
import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.custom.ProductAlreadyPresentException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.repository.CategoryRepository;
import com.answerdigital.answerking.request.CategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
final class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductService productService;

    private final CategoryTestBuilder categoryTestBuilder;

    private final CategoryRequestTestBuilder categoryRequestTestBuilder;

    private final ProductTestBuilder productTestBuilder;

    private static final Long NONEXISTENT_CATEGORY_ID = 2L;

    private CategoryServiceTest() {
        categoryTestBuilder = new CategoryTestBuilder();
        categoryRequestTestBuilder = new CategoryRequestTestBuilder();
        productTestBuilder = new ProductTestBuilder();
    }

    @Test
    void testAddCategory() {
        // given
        final CategoryRequest addCategoryRequest = categoryRequestTestBuilder.withDefaultAddRequestValues().build();
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
        final CategoryRequest addCategoryRequest = categoryRequestTestBuilder.withDefaultAddRequestValues().build();

        // when
        doReturn(true).when(categoryRepository).existsByName(anyString());
        final Exception exception = assertThrows(NameUnavailableException.class,
                () -> categoryService.addCategory(addCategoryRequest));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).existsByName(anyString());
    }

    @Test
    void testUpdateCategory() {
        // given
        final Category existingCategory = categoryTestBuilder.withDefaultValues().build();
        final CategoryRequest updateCategoryRequest = categoryRequestTestBuilder.withDefaultUpdateRequestValues().build();
        final Category expectedResponse = categoryTestBuilder.withDefaultValues()
                .withName(updateCategoryRequest.name())
                .withDescription(updateCategoryRequest.description())
                .build();
        final var categoryResponse = CategoryResponse.builder()
                .id(1L)
                .name("Pizzas")
                .description("Italian style stone baked pizzas.")
                .build();

        // when
        doReturn(false).when(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        doReturn(Optional.of(existingCategory)).when(categoryRepository).findById(anyLong());
        doReturn(expectedResponse).when(categoryRepository).save(any(Category.class));

        final CategoryResponse response = categoryService.updateCategory(updateCategoryRequest, existingCategory.getId());

        // then
        assertEquals(expectedResponse.getName(), response.getName());
        verify(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        verify(categoryRepository).findById(anyLong());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testUpdateCategoryThatDoesNotExist() {
        // given
        final CategoryRequest updateCategoryRequest = categoryRequestTestBuilder.withDefaultUpdateRequestValues().build();

        // when
        doReturn(false).when(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());
        final Exception exception = assertThrows(NotFoundException.class,
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
        final CategoryRequest updateCategoryRequest = categoryRequestTestBuilder.withDefaultUpdateRequestValues().build();

        // when
        doReturn(true).when(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        final Exception exception = assertThrows(NameUnavailableException.class,
                () -> categoryService.updateCategory(updateCategoryRequest, existingCategory.getId()));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
    }

    @Test
    void testAddProductToCategoryThatIsAlreadyInCategory() {
        // given
        final Product product = productTestBuilder.withDefaultValues().build();
        final Category category = categoryTestBuilder.withDefaultValues()
                .withProduct(product)
                .build();

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(product).when(productService).findById(anyLong());
        final Exception exception = assertThrows(ProductAlreadyPresentException.class,
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
        final Exception exception = assertThrows(NotFoundException.class,
                () -> categoryService.addProductToCategory(NONEXISTENT_CATEGORY_ID, product.getId()));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testRemoveProductFromCategory() {
        // given
        final Product product = productTestBuilder.withDefaultValues().build();
        final Category category = categoryTestBuilder.withDefaultValues()
                .withProduct(product)
                .build();
        final Category expectedResponse = categoryTestBuilder.withDefaultValues().build();

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(product).when(productService).findById(anyLong());
        doReturn(expectedResponse).when(categoryRepository).save(any(Category.class));

        final CategoryResponse response = categoryService.removeProductFromCategory(category.getId(), product.getId());

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
        final Exception exception = assertThrows(NotFoundException.class,
                () -> categoryService.removeProductFromCategory(category.getId(), product.getId()));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
        verify(productService).findById(anyLong());
    }

    @Test
    void testRemoveProductFromCategoryThatDoesNotExist() {
        // given
        final Product product = productTestBuilder.withDefaultValues().build();

        // when
        doReturn(Optional.empty()).when(categoryRepository).findById(anyLong());
        final Exception exception = assertThrows(NotFoundException.class,
                () -> categoryService.removeProductFromCategory(NONEXISTENT_CATEGORY_ID, product.getId()));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testRetireCategory() {
        // given
        final Category category = categoryTestBuilder.withDefaultValues().build();
        final Category expectedCategory = categoryTestBuilder.withDefaultValues()
                .withRetired(true)
                .build();
        final var categoryResponse = CategoryResponse.builder()
                .id(1L)
                .name("Pizzas")
                .description("Italian style stone baked pizzas.")
                .build();

        // when
        doReturn(Optional.of(category)).when(categoryRepository).findById(anyLong());
        doReturn(expectedCategory).when(categoryRepository).save(any(Category.class));

        // then
        categoryService.retireCategory(category.getId());

        verify(categoryRepository).findById(category.getId());
        verify(categoryRepository).save(expectedCategory);
    }

    @Test
    void testRetireCategoryAlreadyRetiredThrowsRetirementException() {
        // given
        final Category retiredCategory = categoryTestBuilder.withDefaultValues()
                .withRetired(true)
                .build();

        // when
        doReturn(Optional.of(retiredCategory)).when(categoryRepository).findById(anyLong());

        // then
        assertThrows(RetirementException.class, () -> categoryService.retireCategory(retiredCategory.getId()));
        verify(categoryRepository).findById(retiredCategory.getId());
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
