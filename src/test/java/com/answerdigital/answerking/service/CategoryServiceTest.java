package com.answerdigital.answerking.service;

import com.answerdigital.answerking.builder.CategoryRequestTestBuilder;
import com.answerdigital.answerking.builder.CategoryTestBuilder;
import com.answerdigital.answerking.builder.ProductTestBuilder;
import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.repository.CategoryRepository;
import com.answerdigital.answerking.request.CategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
import com.answerdigital.answerking.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
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
    void testFindByIdReturnsFoundCategory() {
        // given
        final Category category = categoryTestBuilder
            .withDefaultValues()
            .build();

        // when
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        final Category expectedResponse = categoryService.findById(category.getId());

        // then
        assertEquals(expectedResponse, category);
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testFindByIdWithInvalidIdThrowsNotFoundException() {
        // when
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> categoryService.findById(NONEXISTENT_CATEGORY_ID));
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testFindByIdResponseReturnsResponse() {
        // given
        final Category category = categoryTestBuilder
            .withDefaultValues()
            .build();

        // when
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        final CategoryResponse response = categoryService.findByIdResponse(category.getId());

        // then
        assertInstanceOf(CategoryResponse.class, response);
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testFindByIdResponseWithInvalidCategoryIdThrowsNotFoundException() {
        // when
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> categoryService.findByIdResponse(NONEXISTENT_CATEGORY_ID));
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void testFindAllReturnsListOfCategoryResponses() {
        // given
        final Set<Category> categories = Set.of(
            categoryTestBuilder
                .withDefaultValues()
                .build()
        );

        // when
        when(categoryRepository.findAll()).thenReturn(categories);
        final Set<CategoryResponse> expectedResponse = categoryService.findAll();

        // then
        assertInstanceOf(HashSet.class, expectedResponse);
        assertEquals(expectedResponse.size(), categories.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void testFindAllWithNoCategoriesReturnsEmptyList() {
        // when
        when(categoryRepository.findAll()).thenReturn(new HashSet<>());
        final Set<CategoryResponse> expectedResponse = categoryService.findAll();

        // then
        assertEquals(0, expectedResponse.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void testAddCategory() {
        // given
        final CategoryRequest addCategoryRequest = categoryRequestTestBuilder
            .withDefaultValues()
            .build();
        final Category expectedResponse = categoryTestBuilder
            .withDefaultValues()
            .build();

        // when
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        doReturn(expectedResponse).when(categoryRepository).save(any(Category.class));
        final CategoryResponse categoryResponse = categoryService.addCategory(addCategoryRequest);

        // then
        assertAll(
            () -> assertEquals(expectedResponse.getId(), categoryResponse.getId()),
            () -> assertEquals(expectedResponse.getName(), categoryResponse.getName()),
            () -> assertEquals(expectedResponse.getDescription(), categoryResponse.getDescription())
        );
        verify(categoryRepository).existsByName(anyString());
        verify(categoryRepository, atLeast(2)).save(any(Category.class));
    }

    @Test
    void testAddCategoryThatAlreadyExists() {
        // given
        final CategoryRequest addCategoryRequest = categoryRequestTestBuilder
            .withDefaultValues()
            .build();

        // when
        when(categoryRepository.existsByName(anyString())).thenReturn(true);

        // then
        assertThrows(NameUnavailableException.class, () -> categoryService.addCategory(addCategoryRequest));
        verify(categoryRepository).existsByName(anyString());
    }

    @Test
    void testAddCategoryContainsRetiredProductThrowsRetirementException() {
        // given
        final Product product = productTestBuilder
            .withId(1L)
            .withDefaultValues()
            .withRetired(true)
            .build();
        final Category category = categoryTestBuilder
            .withDefaultValues()
            .build();
        final CategoryRequest categoryRequest = categoryRequestTestBuilder
            .withProductIds(List.of(product.getId()))
            .build();

        // when
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(productService.findAllProductsInListOfIds(Mockito.<Long>anyList())).thenReturn(List.of(product));

        // then
        assertThrows(RetirementException.class, () -> categoryService.addCategory(categoryRequest));
        verify(categoryRepository).save(any(Category.class));
        verify(productService).findAllProductsInListOfIds(Mockito.<Long>anyList());
    }

    @Test
    void testUpdateCategory() {
        // given
        final Category existingCategory = categoryTestBuilder
            .withDefaultValues()
            .build();
        final CategoryRequest updateCategoryRequest = categoryRequestTestBuilder
            .withDefaultValues()
            .build();
        final Category expectedResponse = categoryTestBuilder
            .withDefaultValues()
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
        verify(categoryRepository, atLeast(2)).save(any(Category.class));
    }

    @Test
    void testUpdateCategoryThatDoesNotExist() {
        // given
        final CategoryRequest updateCategoryRequest = categoryRequestTestBuilder.withDefaultValues().build();

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
        final Category existingCategory = categoryTestBuilder
            .withDefaultValues()
            .build();
        final CategoryRequest updateCategoryRequest = categoryRequestTestBuilder
            .withDefaultValues()
            .build();
        final Long existingCategoryId = existingCategory.getId();

        // when
        when(categoryRepository.existsByNameAndIdIsNot(anyString(), anyLong())).thenReturn(true);
        final Exception exception = assertThrows(NameUnavailableException.class,
            () -> categoryService.updateCategory(updateCategoryRequest, existingCategoryId));

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
        final Long retiredCategoryId = retiredCategory.getId();

        // when
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(retiredCategory));

        // then
        assertThrows(RetirementException.class, () -> categoryService.retireCategory(retiredCategoryId));
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

    @Test
    void findProductsByCategoryIdWithValidIdReturnsListOfProductResponses() {
        // given
        final Category category = categoryTestBuilder
            .withDefaultValues()
            .build();
        final List<ProductResponse> productResponses = new ArrayList<>();

        // when
        when(productService.findProductsByCategoryId(category.getId()))
            .thenReturn(productResponses);
        List<ProductResponse> response = categoryService.findProductsByCategoryId(category.getId());

        // then
        assertEquals(productResponses, response);
        verify(productService).findProductsByCategoryId(category.getId());
    }
}
