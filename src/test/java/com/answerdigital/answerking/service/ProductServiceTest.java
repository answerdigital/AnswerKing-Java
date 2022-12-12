package com.answerdigital.answerking.service;

import com.answerdigital.answerking.builder.CategoryTestBuilder;
import com.answerdigital.answerking.builder.ProductRequestTestBuilder;
import com.answerdigital.answerking.builder.ProductTestBuilder;
import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.repository.ProductRepository;
import com.answerdigital.answerking.request.ProductRequest;
import com.answerdigital.answerking.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
final class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    private final ProductTestBuilder productTestBuilder;

    private final ProductRequestTestBuilder productRequestTestBuilder;

    private final CategoryTestBuilder categoryTestBuilder;

    private static final long NONEXISTENT_PRODUCT_ID = 10L;

    private ProductServiceTest() {
        productTestBuilder = new ProductTestBuilder();
        productRequestTestBuilder = new ProductRequestTestBuilder();
        categoryTestBuilder = new CategoryTestBuilder();
    }

    @Test
    void addNewProductReturnsProductObjectSuccessfully() {
        // given
        final Product expectedProduct = productTestBuilder.withDefaultValues().build();
        final ProductRequest productRequest = productRequestTestBuilder.withDefaultValues().build();
        final Category category = categoryTestBuilder.withDefaultValues().build();

        // when
        when(productRepository.save(any())).thenReturn(expectedProduct);
        when(productRepository.existsByName(any())).thenReturn(false);
        when(categoryService.findById(any())).thenReturn(category);

        final ProductResponse productResponse = productService.addNewProduct(productRequest);

        // then
        assertEquals(expectedProduct.getName(), productResponse.getName());
        assertEquals(expectedProduct.getPrice().toString(), productResponse.getPrice().toString());
        verify(productRepository).save(any());
    }

    @Test
    void addNewProductThrowsExceptionIfProductNameAlreadyExist() {
        // given
        final ProductRequest productRequest = productRequestTestBuilder.withDefaultValues().build();

        // when
        when(productRepository.existsByName(any())).thenReturn(true);

        // then
        assertThrows(NameUnavailableException.class, () -> productService.addNewProduct(productRequest));
    }

    @Test
    void getProductByIdReturnsProductObject() {
        // given
        final Product expectedProduct = productTestBuilder.withDefaultValues().build();

        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(expectedProduct));

        final Product productResult = productService.findById(expectedProduct.getId());

        // then
        assertEquals(expectedProduct.getName(), productResult.getName());
        assertEquals(expectedProduct.getPrice().toString(), productResult.getPrice().toString());
        verify(productRepository).findById(any());
    }

    @Test
    void getProductByIdReturnsNotFoundExceptionIfProductIdDoesNotExist() {
        assertThrows(NotFoundException.class, () -> productService.findById(NONEXISTENT_PRODUCT_ID));
    }

    @Test
    void getAllProductsReturnsListOfProductObjects() {
        // given
        final Product expectedProductOne = productTestBuilder.withDefaultValues().build();
        final Product expectedProductTwo = productTestBuilder.withDefaultValues()
                .withId(2L)
                .withName("Fries")
                .build();

        // when
        when(productRepository.findAll()).thenReturn(List.of(expectedProductOne, expectedProductTwo));

        final List<ProductResponse> productResponses = productService.findAll();

        // then
        assertAll(
                () -> assertFalse(productResponses.isEmpty()),
                () -> assertEquals(productResponses.get(0).getName(), expectedProductOne.getName()),
                () -> assertEquals(productResponses.get(1).getName(), expectedProductTwo.getName())
        );
        verify(productRepository).findAll();
    }

    @Test
    void updateProductReturnsProductObjectSuccessfully() {
        // given
        final Product expectedProduct = productTestBuilder.withDefaultValues().build();
        final ProductRequest productRequest = productRequestTestBuilder.withDefaultValues().build();

        // when
        when(productRepository.save(any())).thenReturn(expectedProduct);
        when(productRepository.existsByNameAndIdIsNot(any(), anyLong())).thenReturn(false);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(expectedProduct));

        final ProductResponse productResponse = productService.updateProduct(expectedProduct.getId(), productRequest);

        // then
        assertEquals(expectedProduct.getName(), productResponse.getName());
        assertEquals(expectedProduct.getPrice().toString(), productResponse.getPrice().toString());
        verify(productRepository).save(any());
    }

    @Test
    void updateProductThrowsExceptionIfProductIdDoesNotExist() {
        // given
        final ProductRequest productRequest = productRequestTestBuilder.withDefaultValues().build();

        // then
        assertThrows(NotFoundException.class,
                () -> productService.updateProduct(NONEXISTENT_PRODUCT_ID, productRequest));
    }

    @Test
    void updateProductThrowsExceptionIfProductNameAlreadyExist() {
        // given
        final Product existingProduct = productTestBuilder.withDefaultValues().build();
        final ProductRequest productRequest = productRequestTestBuilder.withDefaultValues().build();

        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(existingProduct));
        when(productRepository.existsByNameAndIdIsNot(any(), anyLong())).thenReturn(true);

        //then
        assertThrows(NameUnavailableException.class,
                () -> productService.updateProduct(existingProduct.getId(), productRequest));
    }

    @Test
    void testRetireProduct() {
        // when
        final Product product = productTestBuilder.withDefaultValues().build();

        // then
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.retireProduct(product.getId());
        product.setRetired(true);

        verify(productRepository).findById(anyLong());
        verify(productRepository).save(product);
    }

    @Test
    void testRetireProductAlreadyRetiredThrowsRetirementException() {
        // given
        final Product product = productTestBuilder.withDefaultValues().withRetired(true).build();

        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // then
        assertThrows(RetirementException.class, () -> productService.retireProduct(product.getId()));
        verify(productRepository).findById(anyLong());
    }

    @Test
    void testRetireProductDoesNotExistThrowsNotFoundException() {
        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> productService.retireProduct(NONEXISTENT_PRODUCT_ID));
        verify(productRepository).findById(anyLong());
    }
}

