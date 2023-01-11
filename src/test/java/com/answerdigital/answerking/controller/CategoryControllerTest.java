package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.builder.CategoryRequestTestBuilder;
import com.answerdigital.answerking.builder.CategoryResponseTestBuilder;
import com.answerdigital.answerking.builder.ProductResponseTestBuilder;
import com.answerdigital.answerking.builder.SimpleCategoryResponseTestBuilder;
import com.answerdigital.answerking.repository.ProductRepository;
import com.answerdigital.answerking.request.CategoryRequest;
import com.answerdigital.answerking.response.CategoryResponse;
import com.answerdigital.answerking.response.ProductResponse;
import com.answerdigital.answerking.response.SimpleCategoryResponse;
import com.answerdigital.answerking.service.CategoryService;
import com.answerdigital.answerking.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CategoryService categoryService;

    @MockBean
    ProductService productService;

    @MockBean
    ProductRepository productRepository;

    private final SimpleCategoryResponseTestBuilder simpleCategoryResponseTestBuilder;

    private final CategoryRequestTestBuilder categoryRequestTestBuilder;

    private final CategoryResponseTestBuilder categoryResponseTestBuilder;

    private final ProductResponseTestBuilder productResponseTestBuilder;

    public CategoryControllerTest() {
        simpleCategoryResponseTestBuilder = new SimpleCategoryResponseTestBuilder();
        categoryRequestTestBuilder = new CategoryRequestTestBuilder();
        categoryResponseTestBuilder = new CategoryResponseTestBuilder();
        productResponseTestBuilder = new ProductResponseTestBuilder();
    }

    @Test
    void addCategoryTest() throws Exception {
        final LocalDateTime testDate = LocalDateTime.now();

        final CategoryRequest categoryRequest = categoryRequestTestBuilder
            .withDefaultValues()
            .build();

        final CategoryResponse categoryResponse = categoryResponseTestBuilder
            .withDefaultValues()
            .withName(categoryRequest.name())
            .withDescription(categoryRequest.description())
            .withCreatedOn(testDate)
            .withLastUpdated(testDate)
            .build();

        final String categoryRequestJson =
            "{\"name\": \"" + categoryRequest.name() + "\",\"description\": \"" + categoryRequest.description() + "\"}";

        doReturn(categoryResponse).when(categoryService).addCategory(any(CategoryRequest.class));
        final var response = mvc.perform(post("/categories")
                        .content(categoryRequestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse();
        final var resultJsonNode = objectMapper.readTree(response.getContentAsString());

        assertFalse(response.getContentAsString().isEmpty());
        assertEquals(categoryRequest.name(), resultJsonNode.get("name").textValue());
        assertEquals(categoryRequest.description(), resultJsonNode.get("description").textValue());
    }

    @Test
    void fetchProductsByCategoryTest() throws Exception {
        final SimpleCategoryResponse simpleCategoryResponse = simpleCategoryResponseTestBuilder
            .withDefaultValues()
            .build();

        final ProductResponse productResponse = productResponseTestBuilder
            .withDefaultValues()
            .withName(simpleCategoryResponse.getName())
            .withDescription(simpleCategoryResponse.getDescription())
            .withCategory(simpleCategoryResponse)
            .build();

        doReturn(List.of(productResponse)).when(categoryService).findProductsByCategoryId(anyLong());
        final var response = mvc.perform(get("/categories//{categoryId}/products", 1L)).andExpect(status().isOk());

        final var responseRecord = objectMapper.readTree(response.andReturn().getResponse().getContentAsString()).get(0);

        assertAll(
                () -> assertEquals(productResponse.getId(), responseRecord.get("id").asLong()),
                () -> assertEquals(simpleCategoryResponse.getName(), responseRecord.get("name").textValue()),
                () -> assertEquals(simpleCategoryResponse.getId(), responseRecord.get("category").get("id").asLong())
        );
    }

    @Test
    void addCategoryWithInvalidCategoryRequestNameTest() throws Exception {
        final var categoryRequest = "{\"name\": \"2134214\",\"description\": \"random description\"}";

        final String error = Objects.requireNonNull(mvc.perform(post("/categories")
            .content(categoryRequest)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
            .andReturn().getResolvedException()).getMessage();

        assertTrue(error.contains("Category name must only contain letters, spaces and dashes"));
    }

    @Test
    void addCategoryWithInvalidCategoryRequestDescTest() throws Exception {
        final var categoryRequest = "{\"name\": \"random name\",\"description\": \"random description #\"}";

        final String error = Objects.requireNonNull(mvc.perform(post("/categories")
            .content(categoryRequest)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
            .andReturn().getResolvedException()).getMessage();

        assertTrue(error.contains("Category description can only contain letters, numbers, spaces and !?-.,' punctuation"));
    }

    @Test
    void updateCategoryTest() throws Exception {
        // given
        final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        final CategoryRequest categoryRequest = categoryRequestTestBuilder
            .withDefaultValues()
            .build();

        final LocalDateTime testDate = LocalDateTime.now();

        final CategoryResponse categoryResponse = categoryResponseTestBuilder
            .withDefaultValues()
            .withName(categoryRequest.name())
            .withDescription(categoryRequest.description())
            .withProductIds(new ArrayList<>())
            .withLastUpdated(testDate)
            .withCreatedOn(testDate)
            .withIsRetired(false)
            .build();

        final String categoryRequestJson =
            "{\"name\": \"" + categoryRequest.name() + "\",\"description\": \"" + categoryRequest.description() + "\"}";

        // when
        doReturn(categoryResponse).when(categoryService).updateCategory(any(CategoryRequest.class), anyLong());

        final var httpResponse = mvc.perform(put("/categories/{categoryId}", categoryResponse.getId())
                                                     .content(categoryRequestJson)
                                                     .contentType(MediaType.APPLICATION_JSON))
                                                     .andExpect(status().isOk())
                                                     .andReturn()
                                                     .getResponse();

        final var resultJsonNode = mapper.readTree(httpResponse.getContentAsString());

        // then
        assertFalse(httpResponse.getContentAsString().isEmpty());
        assertEquals(categoryRequest.name(), resultJsonNode.get("name").textValue());
        assertEquals(categoryRequest.description(), resultJsonNode.get("description").textValue());
    }

    @Test
    void updateCategoryWithInvalidCategoryRequestNameTest() throws Exception {
        final String categoryRequestJson = "{\"name\": \"2134214\",\"description\": \"random description\"}";

        final String error = Objects.requireNonNull(mvc.perform(put("/categories/{categoryId}", 112L)
            .content(categoryRequestJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
            .andReturn().getResolvedException()).getMessage();

        assertTrue(error.contains("Category name must only contain letters, spaces and dashes"));
    }

    @Test
    void updateCategoryWithInvalidCategoryRequestDescTest() throws Exception {
        final var categoryRequest = "{\"name\": \"random name\",\"description\": \"random description #\"}";

        final String error = Objects.requireNonNull(mvc.perform(put("/categories/{categoryId}", 112L)
            .content(categoryRequest)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
            .andReturn().getResolvedException()).getMessage();

        assertTrue(error.contains("Category description can only contain letters, numbers, spaces and !?-.,' punctuation"));
    }
}
