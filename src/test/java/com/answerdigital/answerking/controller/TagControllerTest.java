package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.builder.TagRequestTestBuilder;
import com.answerdigital.answerking.builder.TagTestBuilder;
import com.answerdigital.answerking.model.Tag;
import com.answerdigital.answerking.repository.TagRepository;
import com.answerdigital.answerking.request.TagRequest;
import com.answerdigital.answerking.service.TagService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doReturn;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(TagController.class)
class TagControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TagService tagService;

    @MockBean
    private TagRepository tagRepository;

    private static final TagTestBuilder TAG_TEST_BUILDER;

    private static final TagRequestTestBuilder TAG_REQUEST_TEST_BUILDER;

    private static final ObjectMapper OBJECT_MAPPER;

    private static final String TAGS_ENDPOINT = "/tags";

    static {
        TAG_TEST_BUILDER = new TagTestBuilder();
        TAG_REQUEST_TEST_BUILDER = new TagRequestTestBuilder();
        OBJECT_MAPPER = new ObjectMapper();
    }

    @Test
    void getAllTagsReturnListOfTagResponses() throws Exception {
        // given
        final Tag tagOne = TAG_TEST_BUILDER
            .withDefaultValues()
            .build();
        final Tag tagTwo = TAG_TEST_BUILDER
            .withId(2L)
            .withName("")
            .withDescription("")
            .build();

        // when
        doReturn(List.of(tagOne, tagTwo))
            .when(tagService)
            .findAll();

        final RequestBuilder request = MockMvcRequestBuilders.get(TAGS_ENDPOINT);
        final MockHttpServletResponse servletResponse = mvc.perform(request).andReturn().getResponse();
        final JsonNode jsonNodeResponse = OBJECT_MAPPER.readTree(servletResponse.getContentAsString());

        // then
        assertEquals(HttpStatus.OK.value(), servletResponse.getStatus());
        assertFalse(servletResponse.getContentAsString().isEmpty());
        assertEquals(tagOne.getId(), jsonNodeResponse.get(0).get("id").asLong());
    }
}
