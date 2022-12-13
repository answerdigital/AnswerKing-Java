package com.answerdigital.answerking.service;

import com.answerdigital.answerking.builder.CategoryRequestTestBuilder;
import com.answerdigital.answerking.builder.CategoryTestBuilder;
import com.answerdigital.answerking.builder.ProductTestBuilder;
import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.repository.CategoryRepository;
import com.answerdigital.answerking.request.CategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private static final Long NONEXISTENT_CATEGORY_ID = 10L;

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
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(expectedResponse);
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
        when(categoryRepository.existsByName(anyString())).thenReturn(true);
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

        // when
        when(categoryRepository.existsByNameAndIdIsNot(anyString(), anyLong())).thenReturn(false);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(expectedResponse);

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
        when(categoryRepository.existsByNameAndIdIsNot(anyString(), anyLong())).thenReturn(false);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
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
        when(categoryRepository.existsByNameAndIdIsNot(anyString(), anyLong())).thenReturn(true);
        final Exception exception = assertThrows(NameUnavailableException.class,
                () -> categoryService.updateCategory(updateCategoryRequest, existingCategory.getId()));

        // then
        assertFalse(exception.getMessage().isEmpty());
        verify(categoryRepository).existsByNameAndIdIsNot(anyString(), anyLong());
    }
    
    @Test
    void testRetireCategory() {
        // given
        final Category category = categoryTestBuilder.withDefaultValues().build();
        final Category expectedCategory = categoryTestBuilder.withDefaultValues()
                .withRetired(true)
                .build();

        // when
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(expectedCategory);

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
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(retiredCategory));

        // then
        assertThrows(RetirementException.class, () -> categoryService.retireCategory(retiredCategory.getId()));
        verify(categoryRepository).findById(retiredCategory.getId());
    }

    @Test
    void testRetireCategoryDoesNotExistThrowsNotFoundException() {
        // when
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> categoryService.retireCategory(NONEXISTENT_CATEGORY_ID));
        verify(categoryRepository).findById(anyLong());
    }
}
