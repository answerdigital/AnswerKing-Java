package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mvc;

    private Product product;

    @BeforeEach
    public void generateProduct() {
        product = Product.builder()
                .id(55L)
                .name("test")
                .description("testDes")
                .price(BigDecimal.valueOf(2.99))
                .retired(false)
                .build();
    }

    @WithMockUser("paul")
    @Test
    void getAllProductsReturnListOfProductObjects() throws Exception {
        //given
        given(productService.findAll()).willReturn(List.of(product));

        //when
        RequestBuilder request = MockMvcRequestBuilders.get("/products");

        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertFalse(response.getContentAsString().isEmpty());
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(product.getId(), mapper.readTree(response.getContentAsString()).get(0).get("id").asLong());
    }

    @WithMockUser("paul")
    @Test
    void getAllProductsReturnNoContentIfEmpty() throws Exception {
        //when
        mvc.perform(get("/products"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser("paul")
    @Test
    void getProductByIdReturnsOkStatusIfExist() throws Exception {
        //given
        when(productService.findById(55L)).thenReturn(product);
        //when
        ResultActions actualPerformResult = mvc.perform(get("/products/{id}", 55L)).andExpect(status().isOk());
        ObjectMapper mapper = new ObjectMapper();
        //then
        assertEquals(product.getId(), mapper.readTree(actualPerformResult.andReturn()
                .getResponse().getContentAsString()).get("id").asLong());
    }

    @WithMockUser("paul")
    @Test
    void getProductByIdReturnsNotFoundExceptionIfIdDoesNotExist() throws Exception {
        mvc.perform(get("/{id}", 55L)).andExpect(status().isNotFound());
    }

    @WithMockUser("paul")
    @Test
    void addProductWithInvalidNameThrowsException() throws Exception {
        //given
        String newProduct = "{\"name\": \"abc12\",\"description\": \"descTest\",\"price\": \"4.75\"}";
        //when
        final String error = mvc.perform(post("/products")
                        .content(newProduct)
                        .contentType(MediaType.APPLICATION_JSON))
        //then
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResolvedException().getMessage();

        assertTrue(error.contains("Product name must only contain letters, spaces and dashes"));
    }

    @WithMockUser("paul")
    @Test
    void addProductWithValidObjectReturnsProductAndCreatedStatus() throws Exception {
        //given
        String newProduct = "{\"name\": \"test\",\"description\": \"descTest\",\"price\": \"4.75\"}";
        given(productService.addNewProduct(any())).willReturn(product);

        //when
        ResultActions actualPerformResult = mvc.perform(post("/products")
                .content(newProduct)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        actualPerformResult.andExpect(status().isCreated());
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(mapper.readTree(newProduct).get("name"), mapper.readTree(actualPerformResult.andReturn()
                .getResponse().getContentAsString()).get("name"));
    }

    @WithMockUser("paul")
    @Test
    void updateProductWithInvalidPriceThrowsException() throws Exception {
        //given
        String newProduct = "{\"name\": \"abc\",\"description\": \"descTest\",\"price\": \"4.7587\"}";
        //when
        final String error = mvc.perform(MockMvcRequestBuilders.put("/products/{id}", 55L)
                        .content(newProduct)
                        .contentType(MediaType.APPLICATION_JSON))
        //then
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResolvedException().getMessage();

        assertTrue(error.contains("Product price is invalid"));
    }

    @WithMockUser("paul")
    @Test
    void updateProductWithValidObjectReturnsProductAndOkStatus() throws Exception {
        //given
        String newProduct = "{\"name\": \"test\",\"description\": \"descTest\",\"price\": \"4.75\"}";
        given(productService.updateProduct(eq(55L), any())).willReturn(product);
        //when

        ResultActions actualPerformResult = mvc.perform(put("/products/{id}", 55L)
                .content(newProduct)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actualPerformResult.andExpect(status().isOk());
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(mapper.readTree(newProduct).get("name"), mapper.readTree(actualPerformResult.andReturn()
                .getResponse().getContentAsString()).get("name"));
    }
}

