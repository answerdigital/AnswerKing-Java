package com.answerdigital.answerking.service;

import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.repository.ProductRepository;
import com.answerdigital.answerking.request.ProductRequest;
import com.answerdigital.answerking.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private Product product;

    private Category category;

    private ProductRequest productRequest;

    private static final long PRODUCT_ID = 55L;

    @BeforeEach
    public void generateProduct() {
        category = Category.builder()
                .name("test")
                .description("categoryDesc")
                .id(1L)
                .products(new HashSet<>())
                .build();
        product = Product.builder()
                .id(PRODUCT_ID)
                .name("test")
                .description("testDes")
                .price(BigDecimal.valueOf(2.99))
                .retired(false)
                .category(category)
                .build();
        productRequest = ProductRequest.builder()
                .name("test")
                .description("testD")
                .price(BigDecimal.valueOf(1.99))
                .categoryId(1L)
                .build();
    }

    @Test
    void addNewProductReturnsProductObjectSuccessfully() {
        //given
        when(productRepository.save(any())).thenReturn(product);
        when(productRepository.existsByName(any())).thenReturn(false);
        when(categoryService.findById(any())).thenReturn(category);
        //when
        final ProductResponse actualAddNewProductResult = productService.addNewProduct(
                productRequest);
        //then
        assertEquals(product.getName(), actualAddNewProductResult.getName());
        assertEquals(product.getPrice().toString(), actualAddNewProductResult.getPrice().toString());
        verify(productRepository).save(any());
    }

    @Test
    void addNewProductThrowsExceptionIfProductNameAlreadyExist() {
        //given
        when(productRepository.existsByName(any())).thenReturn(true);
        //when
        assertThatThrownBy(() -> productService.addNewProduct(productRequest))
                //then
                .isInstanceOf(NameUnavailableException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void getProductByIdReturnProductObject() {
        //given
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));
        //when
        final Product actualAddNewProductResult = productService.findById(12L);
        //then
        assertEquals(product.getName(), actualAddNewProductResult.getName());
        assertEquals(product.getPrice().toString(), actualAddNewProductResult.getPrice().toString());
        verify(productRepository).findById(any());
    }

    @Test
    void getProductByIdReturnsNotFoundExceptionIfProductIdDoesNotExist() {
        //when
        assertThatThrownBy(() -> productService.findById(15L))
                //then
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("does not exist");
    }

    @Test
    void getAllProductsReturnListOfProductObjects() {
        //given
        when(productRepository.findAll()).thenReturn(List.of(product));
        //when
        final List<ProductResponse> actualResult = productService.findAll();
        //then
        assertFalse(actualResult.isEmpty());
        assertEquals(actualResult.get(0).getName(), product.getName());
        verify(productRepository).findAll();
    }

    @Test
    void updateProductReturnsProductObjectSuccessfully() {
        //given
        when(productRepository.save(any())).thenReturn(product);
        when(productRepository.existsByNameAndIdIsNot(any(), anyLong())).thenReturn(false);
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));

        //when
        final ProductResponse actualAddNewProductResult = productService.updateProduct(12L,
                productRequest);

        //then
        assertEquals(product.getName(), actualAddNewProductResult.getName());
        assertEquals(product.getPrice().toString(), actualAddNewProductResult.getPrice().toString());
        verify(productRepository).save(any());
    }

    @Test
    void updateProductThrowsExceptionIfProductIdDoesNotExist() {
        //when
        assertThatThrownBy(() -> productService.updateProduct(12L, productRequest))
                //then
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("does not exist");
    }

    @Test
    void updateProductThrowsExceptionIfProductNameAlreadyExist() {
        //given
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));
        when(productRepository.existsByNameAndIdIsNot(any(), anyLong())).thenReturn(true);
        //when
        assertThatThrownBy(() -> productService.updateProduct(12L, productRequest))
                //then
                .isInstanceOf(NameUnavailableException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void testRetireProduct() {
        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // then
        productService.retireProduct(PRODUCT_ID);
        product.setRetired(true);

        verify(productRepository).findById(anyLong());
        verify(productRepository).save(product);
    }

    @Test
    void testRetireProductAlreadyRetiredThrowsRetirementException() {
        // given
        final Product expectedProduct = product;
        expectedProduct.setRetired(true);

        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(expectedProduct));

        // then
        assertThrows(RetirementException.class, () -> productService.retireProduct(PRODUCT_ID));
        verify(productRepository).findById(anyLong());
    }

    @Test
    void testRetireProductDoesNotExistThrowsNotFoundException() {
        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> productService.retireProduct(PRODUCT_ID));
        verify(productRepository).findById(anyLong());
    }
}

