package com.answerdigital.academy.answerking.controller;

import com.answerdigital.academy.answerking.model.Category;
import com.answerdigital.academy.answerking.request.AddCategoryRequest;
import com.answerdigital.academy.answerking.request.UpdateCategoryRequest;
import com.answerdigital.academy.answerking.service.CategoryService;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.hamcrest.core.Is.is;
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

    @Test
    void addItemToCategoryTest() throws Exception {
        final var category = Category.builder().build();
        final var itemId = 10L;
        final var categoryId = 20L;

        doReturn(category).when(categoryService).addItemToCategory(categoryId, itemId);

        mvc.perform(put("/categories/{categoryId}/additem/{itemId}", categoryId, itemId))
           .andExpect(status().isOk());
    }

    @Test
    void removeItemFromCategoryTest() throws Exception {
        final var category = Category.builder().build();
        final var itemId = 10L;
        final var categoryId = 20L;

        doReturn(category).when(categoryService).removeItemFromCategory(categoryId, itemId);

        mvc.perform(put("/categories/{categoryId}/removeitem/{itemId}", categoryId, itemId))
           .andExpect(status().isOk());
    }

    @Test
    void addCategoryTest() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        final var addCategoryRequest =  new AddCategoryRequest("random name", "random description");
        final var category = new Category(addCategoryRequest);
        final var  categoryRequest = "{\"name\": \"random name\",\"description\": \"random description\"}";

        doReturn(category).when(categoryService).addCategory(addCategoryRequest);
        final var response = mvc.perform(post("/categories")
                        .content(categoryRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse();
        final var resultJsonNode = mapper.readTree(response.getContentAsString());

        assertFalse(response.getContentAsString().isEmpty());
        assertEquals(addCategoryRequest.name(), resultJsonNode.get("name").textValue());
        assertEquals(addCategoryRequest.description(), resultJsonNode.get("description").textValue());
    }

    @Test
    void addCategoryWithInvalidCategoryRequestNameTest() throws Exception {

        final var  categoryRequest = "{\"name\": \"2134214\",\"description\": \"random description\"}";

        mvc.perform(post("/categories")
           .content(categoryRequest)
           .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.message", is("[Category name must only contain letters, spaces and dashes]")))
           .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void addCategoryWithInvalidCategoryRequestDescTest() throws Exception {

        final var  categoryRequest = "{\"name\": \"random name\",\"description\": \"random description #\"}";

        mvc.perform(post("/categories")
           .content(categoryRequest)
           .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.message", is("[Category description can only contain letters, numbers, spaces and !?-.,' punctuation]")))
           .andExpect(content().contentType(MediaType.APPLICATION_JSON));
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
    }

    @Test
    void updateCategoryWithInvalidCategoryRequestNameTest() throws Exception {

        final var  categoryRequest = "{\"name\": \"2134214\",\"description\": \"random description\"}";

        mvc.perform(put("/categories/{categoryId}", 112L)
           .content(categoryRequest)
           .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.message", is("Category name must only contain letters, spaces and dashes")))
           .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateCategoryWithInvalidCategoryRequestDescTest() throws Exception {

        final var  categoryRequest = "{\"name\": \"random name\",\"description\": \"random description #\"}";

        mvc.perform(put("/categories/{categoryId}", 112L)
           .content(categoryRequest)
           .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.message", is("Category description can only contain letters, numbers, spaces and !?-.,' punctuation")))
           .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
