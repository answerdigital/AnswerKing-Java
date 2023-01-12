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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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

    private static final long NONEXISTENT_PRODUCT_ID = 1L;

    private ProductServiceTest() {
        productTestBuilder = new ProductTestBuilder();
        productRequestTestBuilder = new ProductRequestTestBuilder();
        categoryTestBuilder = new CategoryTestBuilder();
    }

    @Test
    void addNewProductReturnsProductObjectSuccessfully() {
        // given
        final Product product = productTestBuilder
            .withDefaultValues()
            .build();
        final ProductRequest request = productRequestTestBuilder
            .withDefaultValues()
            .build();
        final Category category = categoryTestBuilder
            .withDefaultValues()
            .build();

        // when
        doReturn(category)
            .when(categoryService)
            .findById(anyLong());
        doReturn(false)
            .when(productRepository)
            .existsByName(anyString());
        doReturn(product)
            .when(productRepository)
            .save(any(Product.class));

        final ProductResponse response = productService.addNewProduct(request);

        // then
        assertProductVsProductResponseEquality(product, response);
        verify(categoryService).findById(anyLong());
        verify(productRepository).existsByName(anyString());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void addNewProductThrowsExceptionIfProductNameAlreadyExists() {
        // given
        final ProductRequest productRequest = productRequestTestBuilder
            .withDefaultValues()
            .build();

        // when
        doReturn(true)
            .when(productRepository)
            .existsByName(anyString());

        // then
        assertThrows(NameUnavailableException.class, () -> productService.addNewProduct(productRequest));
        verify(productRepository).existsByName(anyString());
    }

    @Test
    void testFindByIdResponseIsSuccessful() {
        // given
        final Product product = productTestBuilder
            .withDefaultValues()
            .build();

        // when
        doReturn(Optional.of(product))
            .when(productRepository)
            .findById(anyLong());

        final ProductResponse response = productService.findByIdResponse(product.getId());

        // then
        assertProductVsProductResponseEquality(product, response);
        verify(productRepository).findById(anyLong());
    }

    @Test
    void getProductByIdReturnsProductSuccessfully() {
        // given
        final Product product = productTestBuilder
            .withDefaultValues()
            .build();

        // when
        doReturn(Optional.of(product))
            .when(productRepository)
            .findById(anyLong());

        final Product response = productService.findById(product.getId());

        // then
        assertEquals(product, response);
        verify(productRepository).findById(any());
    }

    @Test
    void getProductByIdReturnsNotFoundExceptionIfProductIdDoesNotExist() {
        // when
        doReturn(Optional.empty())
            .when(productRepository)
            .findById(anyLong());

        // then
        assertThrows(NotFoundException.class, () -> productService.findById(NONEXISTENT_PRODUCT_ID));
        verify(productRepository).findById(anyLong());
    }

    @Test
    void getAllProductsReturnsListOfProductObjects() {
        // given
        final Product product = productTestBuilder
            .withDefaultValues()
            .build();
        final Product productTwo = productTestBuilder.withDefaultValues()
            .withId(2L)
            .withName("Fries")
            .build();

        // when
        doReturn(List.of(product, productTwo))
            .when(productRepository)
            .findAll();

        final List<ProductResponse> productResponses = productService.findAll();

        // then
        assertAll(
            () -> assertFalse(productResponses.isEmpty()),
            () -> assertEquals(productResponses.get(0).getName(), product.getName()),
            () -> assertEquals(productResponses.get(1).getName(), productTwo.getName())
        );
        verify(productRepository).findAll();
    }

    @Test
    void updateProductReturnsProductObjectSuccessfully() {
        // given
        final Product product = productTestBuilder
            .withDefaultValues()
            .build();
        final ProductRequest productRequest = productRequestTestBuilder
            .withDefaultValues()
            .build();

        // when
        doReturn(Optional.of(product))
            .when(productRepository)
                .findById(anyLong());
        doReturn(false)
            .when(productRepository)
            .existsByNameAndIdIsNot(anyString(), anyLong());
        doReturn(product)
            .when(productRepository)
            .save(any(Product.class));

        final ProductResponse response = productService.updateProduct(product.getId(), productRequest);

        // then
        assertProductVsProductResponseEquality(product, response);
        verify(productRepository).findById(anyLong());
        verify(productRepository).existsByNameAndIdIsNot(anyString(), anyLong());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProductThrowsExceptionIfProductIdDoesNotExist() {
        // given
        final ProductRequest productRequest = productRequestTestBuilder
            .withDefaultValues()
            .build();

        // then
        assertThrows(NotFoundException.class, () -> productService.updateProduct(NONEXISTENT_PRODUCT_ID, productRequest));
    }

    @Test
    void updateProductThrowsExceptionIfProductNameAlreadyExist() {
        // given
        final Product product = productTestBuilder
            .withDefaultValues()
            .build();
        final ProductRequest productRequest = productRequestTestBuilder
            .withDefaultValues()
            .build();
        final long productId = product.getId();

        // when
        doReturn(Optional.of(product))
            .when(productRepository)
            .findById(anyLong());
        doReturn(true)
            .when(productRepository)
            .existsByNameAndIdIsNot(anyString(), anyLong());

        //then
        assertThrows(NameUnavailableException.class, () -> productService.updateProduct(productId, productRequest));
        verify(productRepository).findById(anyLong());
        verify(productRepository).existsByNameAndIdIsNot(anyString(), anyLong());
    }

    @Test
    void testRetireProduct() {
        // given
        final Product product = productTestBuilder
            .withDefaultValues()
            .build();

        // when
        doReturn(Optional.of(product))
            .when(productRepository)
            .findById(anyLong());
        doReturn(product)
            .when(productRepository)
            .save(any(Product.class));

        productService.retireProduct(product.getId());
        product.setRetired(true);

        // then
        assertTrue(product.isRetired());
        verify(productRepository).findById(anyLong());
        verify(productRepository).save(product);
    }

    @Test
    void testRetireProductAlreadyRetiredThrowsRetirementException() {
        // given
        final Product product = productTestBuilder
            .withDefaultValues()
            .withRetired(true)
            .build();
        final long productId = product.getId();

        // when
        doReturn(Optional.of(product))
            .when(productRepository)
            .findById(anyLong());

        // then
        assertThrows(RetirementException.class, () -> productService.retireProduct(productId));
        verify(productRepository).findById(anyLong());
    }

    @Test
    void testRetireProductDoesNotExistThrowsNotFoundException() {
        // when
        doReturn(Optional.empty())
            .when(productRepository)
            .findById(anyLong());

        // then
        assertThrows(NotFoundException.class, () -> productService.retireProduct(NONEXISTENT_PRODUCT_ID));
        verify(productRepository).findById(anyLong());
    }

    @Test
    void testFindProductsByCategoryIdReturnsListOfProductResponses() {
        // given
        final Product product = productTestBuilder
            .withDefaultValues()
            .build();
        final Product productTwo = productTestBuilder
            .withDefaultValues()
            .withId(2L)
            .withName("Beans & Toast")
            .build();
        final Category category = categoryTestBuilder
            .withDefaultValues()
            .withProducts(Set.of(product, productTwo))
            .build();

        // when
        doReturn(List.of(product, productTwo))
            .when(productRepository)
            .findProductsByCategoryId(anyLong());

        final List<ProductResponse> response = productService.findProductsByCategoryId(category.getId());

        // then
        assertAll("All should be equal",
            () -> assertEquals(2, response.size()),
            () -> assertEquals(response.get(0).getName(), product.getName()),
            () -> assertEquals(response.get(1).getName(), productTwo.getName())
        );
        verify(productRepository).findProductsByCategoryId(category.getId());
    }

    @Test
    void testFindAllProductsInListOfIdsReturnsAListOfProducts() {
        // given
        final Product product = productTestBuilder
            .withDefaultValues()
            .build();
        final Product productTwo = productTestBuilder
            .withDefaultValues()
            .withId(2L)
            .build();

        // when
        doReturn(List.of(product, productTwo))
            .when(productRepository)
            .findAllByIdIn(anyList());

        final List<Product> response = productService.findAllProductsInListOfIds(List.of(
            product.getId(),
            productTwo.getId()
        ));

        // then
        assertAll("All should be equal",
            () -> assertEquals(2, response.size()),
            () -> assertEquals(response.get(0).getName(), product.getName()),
            () -> assertEquals(response.get(1).getName(), productTwo.getName())
        );
        verify(productRepository).findAllByIdIn(anyList());
    }

    /**
     * Helper method which asserts the equality of a Product against a ProductResponse object.
     * @param product The Product to compare.
     * @param response The ProductResponse to compare.
     */
    private void assertProductVsProductResponseEquality(final Product product, final ProductResponse response) {
        assertAll("Product vs ProductResponse Equality",
            () -> assertEquals(product.getId(), response.getId()),
            () -> assertEquals(product.getName(), response.getName()),
            () -> assertEquals(product.getDescription(), response.getDescription()),
            () -> assertEquals(product.getPrice(), response.getPrice()),
            () -> assertEquals(product.getCategory().getName(), response.getCategory().getName()),
            () -> assertEquals(product.isRetired(), response.isRetired())
        );
    }
}
