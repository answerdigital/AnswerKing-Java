package com.answerdigital.academy.answerking.service;

import com.answerdigital.academy.answerking.exception.generic.ConflictException;
import com.answerdigital.academy.answerking.exception.generic.NotFoundException;
import com.answerdigital.academy.answerking.model.Product;
import com.answerdigital.academy.answerking.repository.ProductRepository;
import com.answerdigital.academy.answerking.request.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;
    private Product product;
    private ProductRequest productRequest;

    @BeforeEach
    public void generateProduct() {
        product = Product.builder()
                .id(55L)
                .name("test")
                .description("testDes")
                .price(BigDecimal.valueOf(2.99))
                .retired(false)
                .build();
        productRequest = ProductRequest.builder()
                .name("test")
                .description("testD")
                .price(BigDecimal.valueOf(1.99))
                .available(true)
                .build();
    }

    @Test
    void addNewProductReturnsProductObjectSuccessfully() {
        //given
        when(productRepository.save(any())).thenReturn(product);
        when(productRepository.existsByName(any())).thenReturn(false);
        //when
        Product actualAddNewProductResult = productService.addNewProduct(
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
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void getProductByIdReturnProductObject() {
        //given
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));
        //when
        Product actualAddNewProductResult = productService.findById(12L);
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
        List<Product> actualResult = productService.findAll();
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
        Product actualAddNewProductResult = productService.updateProduct(12L,
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
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already exists");
    }
}

