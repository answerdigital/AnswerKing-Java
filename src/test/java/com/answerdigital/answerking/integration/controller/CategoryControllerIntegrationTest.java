package com.answerdigital.answerking.integration.controller;

import com.answerdigital.answerking.AnswerKingApplication;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;

import com.answerdigital.answerking.response.ProductResponse;
import com.answerdigital.answerking.utility.AbstractContainerBaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {AnswerKingApplication.class, Category.class, Product.class, ProductResponse.class},
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("integration-test")
class CategoryControllerIntegrationTest extends AbstractContainerBaseTest {

    private static final Long CATEGORY_ID = 1L;

    @Autowired
    private MockMvc mvc;

    @Test
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/category_controller_test_input.sql"})
    void getAllProductsByCategoryTest() throws Exception {

        final ObjectMapper mapper = new ObjectMapper();

        final var response = mvc.perform(get("/categories/{categoryId}/products", CATEGORY_ID))
                                                    .andExpect(status().isOk())
                                                    .andReturn()
                                                    .getResponse();

        final var responseContent = response.getContentAsString();

        assertAll(
              () -> assertFalse(responseContent.isEmpty()),
              () -> assertEquals(2, mapper.readTree(responseContent).size()),
              () -> assertEquals("Burger", mapper.readTree(responseContent).get(0).get("name").textValue()),
              () -> assertEquals("300g Beef", mapper.readTree(responseContent).get(0).get("description").textValue()),
              () -> assertEquals(6.69, mapper.readTree(responseContent).get(0).get("price").asDouble()),
              () -> assertEquals("Fries", mapper.readTree(responseContent).get(1).get("name").textValue())
        );
    }

}
