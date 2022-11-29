package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.repository.ProductRepository;
import com.answerdigital.answerking.request.AddCategoryRequest;
import com.answerdigital.answerking.request.UpdateCategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
import com.answerdigital.answerking.response.ProductResponse;
import com.answerdigital.answerking.service.CategoryService;
import com.answerdigital.answerking.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.answerdigital.answerking.util.DateTimeUtility.getDateTimeAsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    CategoryService categoryService;

    @MockBean
    ProductService productService;

    @MockBean
    ProductRepository productRepository;

    @Test
    void addProductToCategoryTest() throws Exception {
        final var category = Category.builder().build();
        final var productId = 10L;
        final var categoryId = 20L;

        doReturn(category).when(categoryService).addProductToCategory(categoryId, productId);

        mvc.perform(put("/categories/{categoryId}/addproduct/{productId}", categoryId, productId))
           .andExpect(status().isOk());
    }

    @Test
    void removeProductFromCategoryTest() throws Exception {
        final var category = Category.builder().build();
        final var productId = 10L;
        final var categoryId = 20L;

        doReturn(category).when(categoryService).removeProductFromCategory(categoryId, productId);

        mvc.perform(put("/categories/{categoryId}/removeproduct/{productId}", categoryId, productId))
           .andExpect(status().isOk());
    }

    @Test
    void addCategoryTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        final String testDate = getDateTimeAsString();
        final var addCategoryRequest =  new AddCategoryRequest("random name", "random description");
        final var categoryResponse = CategoryResponse.builder()
                                                     .name(addCategoryRequest.name())
                                                     .description(addCategoryRequest.description())
                                                     .createdOn(testDate)
                                                     .build();
        final var categoryRequest = "{\"name\": \"random name\",\"description\": \"random description\"}";

        doReturn(categoryResponse).when(categoryService).addCategory(addCategoryRequest);
        final var response = mvc.perform(post("/categories")
                        .content(categoryRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse();
        final var resultJsonNode = mapper.readTree(response.getContentAsString());

        assertFalse(response.getContentAsString().isEmpty());
        assertEquals(addCategoryRequest.name(), resultJsonNode.get("name").textValue());
        assertEquals(addCategoryRequest.description(), resultJsonNode.get("description").textValue());
        assertEquals(testDate.split(" ")[0], resultJsonNode.get("createdOn").textValue().split(" ")[0]);
    }

    @Test
    void fetchProductsByCategoryTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        final var productResponse = ProductResponse.builder()
                                                                  .id(33L)
                                                                  .name("random name")
                                                                  .description("random description")
                                                                  .categories(List.of(22L))
                                                                  .build();

        doReturn(List.of(productResponse)).when(categoryService).findProductsByCategoryId(1L);
        final var response = mvc.perform(get("/categories//{categoryId}/products", 1L)).andExpect(status().isOk());

        final var responseRecord = mapper.readTree(response.andReturn().getResponse().getContentAsString()).get(0);
        assertAll(
                () -> assertEquals(33L, responseRecord.get("id").asLong()),
                () -> assertEquals("random name", responseRecord.get("name").textValue()),
                () -> assertEquals(22L, responseRecord.get("categories").get(0).asLong())
        );
    }

    @Test
    void addCategoryWithInvalidCategoryRequestNameTest() throws Exception {
        final var categoryRequest = "{\"name\": \"2134214\",\"description\": \"random description\"}";

        final String error = mvc.perform(post("/categories")
                        .content(categoryRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResolvedException().getMessage();

        assertTrue(error.contains("Category name must only contain letters, spaces and dashes"));
    }

    @Test
    void addCategoryWithInvalidCategoryRequestDescTest() throws Exception {
        final var categoryRequest = "{\"name\": \"random name\",\"description\": \"random description #\"}";

        final String error = mvc.perform(post("/categories")
                        .content(categoryRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResolvedException().getMessage();

        assertTrue(error.contains("Category description can only contain letters, numbers, spaces and !?-.,' punctuation"));
    }

    @Test
    void updateCategoryTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        final var updateCategoryRequest =  new UpdateCategoryRequest("random name", "random description");
        final var newRandomName = "new random name";
        final var newRandomDesc = "new random description";
        final var category = new Category(newRandomName, newRandomDesc);
        final var categoryId = 112L;
        final var  updateCategoryRequestJson = "{\"name\": \"random name\",\"description\": \"random description\"}";
        String testDate = getDateTimeAsString();

        doReturn(category).when(categoryService).updateCategory(updateCategoryRequest, categoryId);
        final var response = mvc.perform(put("/categories/{categoryId}", categoryId)
                                                     .content(updateCategoryRequestJson)
                                                     .contentType(MediaType.APPLICATION_JSON))
                                                     .andExpect(status().isOk())
                                                     .andReturn()
                                                     .getResponse();
        final var resultJsonNode = mapper.readTree(response.getContentAsString());

        assertFalse(response.getContentAsString().isEmpty());
        assertEquals(newRandomName, resultJsonNode.get("name").textValue());
        assertEquals(newRandomDesc, resultJsonNode.get("description").textValue());
        assertEquals(testDate.split(" ")[0], resultJsonNode.get("lastUpdated").textValue().split(" ")[0]);
    }

    @Test
    void updateCategoryWithInvalidCategoryRequestNameTest() throws Exception {
        final var categoryRequest = "{\"name\": \"2134214\",\"description\": \"random description\"}";

        final String error = mvc.perform(put("/categories/{categoryId}", 112L)
                        .content(categoryRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResolvedException().getMessage();

        assertTrue(error.contains("Category name must only contain letters, spaces and dashes"));
    }

    @Test
    void updateCategoryWithInvalidCategoryRequestDescTest() throws Exception {
        final var categoryRequest = "{\"name\": \"random name\",\"description\": \"random description #\"}";

        final String error = mvc.perform(put("/categories/{categoryId}", 112L)
                        .content(categoryRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResolvedException().getMessage();

        assertTrue(error.contains("Category description can only contain letters, numbers, spaces and !?-.,' punctuation"));
    }

}
