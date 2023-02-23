package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.builder.product.ProductTestBuilder;
import com.answerdigital.answerking.builder.tag.TagRequestTestBuilder;
import com.answerdigital.answerking.builder.tag.TagTestBuilder;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.model.Tag;
import com.answerdigital.answerking.request.TagRequest;
import com.answerdigital.answerking.response.TagResponse;
import com.answerdigital.answerking.service.TagService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static com.answerdigital.answerking.utility.MappingUtility.tagToResponse;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(TagController.class)
class TagControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TagService tagService;

    private static final TagTestBuilder TAG_TEST_BUILDER;

    private static final TagRequestTestBuilder TAG_REQUEST_TEST_BUILDER;

    private static final ProductTestBuilder PRODUCT_TEST_BUILDER;

    private static final ObjectMapper OBJECT_MAPPER;

    private static final String TAGS_ENDPOINT = "/tags";

    static {
        TAG_TEST_BUILDER = new TagTestBuilder();
        TAG_REQUEST_TEST_BUILDER = new TagRequestTestBuilder();
        PRODUCT_TEST_BUILDER = new ProductTestBuilder();
        OBJECT_MAPPER = new ObjectMapper();

        OBJECT_MAPPER.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
    }

    @Test
    void getAllTagsReturnListOfTagResponses() throws Exception {
        // given
        final Product product = PRODUCT_TEST_BUILDER
            .withDefaultValues()
            .build();
        final Tag veganTag = TAG_TEST_BUILDER
            .withDefaultValues()
            .build();
        final Tag glutenFreeTag = TAG_TEST_BUILDER
            .withId(2L)
            .withName("Gluten Free")
            .withDescription("This is gluten free.")
            .withProduct(product)
            .build();

        // when
        doReturn(List.of(veganTag, glutenFreeTag))
            .when(tagService)
            .findAll();

        final RequestBuilder request = MockMvcRequestBuilders.get(TAGS_ENDPOINT);
        final MockHttpServletResponse servletResponse = mvc.perform(request).andReturn().getResponse();
        final JsonNode jsonNodeResponse = OBJECT_MAPPER.readTree(servletResponse.getContentAsString());

        // then
        assertEquals(HttpStatus.OK.value(), servletResponse.getStatus());
        assertFalse(servletResponse.getContentAsString().isEmpty());

        assertAll("veganTag json response equality",
            () -> assertEquals(veganTag.getId(), jsonNodeResponse.get(0).get("id").asLong()),
            () -> assertEquals(veganTag.getName(), jsonNodeResponse.get(0).get("name").asText()),
            () -> assertEquals(veganTag.getDescription(), jsonNodeResponse.get(0).get("description").asText())
        );

        assertAll("glutenFreeTag json response equality",
            () -> assertEquals(glutenFreeTag.getId(), jsonNodeResponse.get(1).get("id").asLong()),
            () -> assertEquals(glutenFreeTag.getName(), jsonNodeResponse.get(1).get("name").asText()),
            () -> assertEquals(glutenFreeTag.getDescription(), jsonNodeResponse.get(1).get("description").asText()),
            () -> assertTrue(jsonNodeResponse.get(1).get("products").isArray()),
            () -> assertFalse(jsonNodeResponse.get(1).get("products").isEmpty()),
            () -> assertEquals(
                glutenFreeTag.getProducts().get(0).getName(),
                jsonNodeResponse.get(1).get("products").get(0).get("name").asText()
            )
        );

        verify(tagService).findAll();
    }

    @Test
    void getAllTagsReturnEmptyList() throws Exception {
        // when
        doReturn(List.of())
            .when(tagService)
            .findAll();

        final RequestBuilder request = MockMvcRequestBuilders.get(TAGS_ENDPOINT);
        final MockHttpServletResponse servletResponse = mvc.perform(request).andReturn().getResponse();
        final JsonNode jsonNodeResponse = OBJECT_MAPPER.readTree(servletResponse.getContentAsString());

        // then
        assertEquals(HttpStatus.OK.value(), servletResponse.getStatus());
        assertFalse(servletResponse.getContentAsString().isEmpty());
        assertEquals("[]", jsonNodeResponse.toString());

        verify(tagService).findAll();
    }

    @Test
    void addTagWithValidProductReturnsTagResponse() throws Exception {
        // given
        final Product product = PRODUCT_TEST_BUILDER
            .withDefaultValues()
            .build();
        final Tag tag = TAG_TEST_BUILDER
            .withDefaultValues()
            .withProduct(product)
            .build();
        final TagResponse tagResponse = tagToResponse(tag);

        // when
        final String json = "{\"name\": \"Vegan\", \"description\": \"This is suitable for Vegans.\", \"products\": [1]}";

        doReturn(tagResponse)
            .when(tagService)
            .addTag(any(TagRequest.class));

        final RequestBuilder request = MockMvcRequestBuilders
            .post(TAGS_ENDPOINT)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON);
        final MockHttpServletResponse servletResponse = mvc.perform(request).andReturn().getResponse();
        final JsonNode jsonNodeResponse = OBJECT_MAPPER.readTree(servletResponse.getContentAsString());

        // then
        assertEquals(HttpStatus.CREATED.value(), servletResponse.getStatus());
        assertFalse(servletResponse.getContentAsString().isEmpty());
        assertEquals(tagResponse.getId(), jsonNodeResponse.get("id").asLong());
        assertEquals(tagResponse.getName(), jsonNodeResponse.get("name").asText());
        assertEquals(tagResponse.getDescription(), jsonNodeResponse.get("description").asText());
        assertEquals(tagResponse.getProductIds().get(0), jsonNodeResponse.get("products").get(0).asLong());

        verify(tagService).addTag(any(TagRequest.class));
    }

    @Test
    void retireTagReturnsNoContent() throws Exception {
        mvc.perform(delete("/tags/{tagId}", 1L))
                .andExpect(status().isNoContent());
    }
}
