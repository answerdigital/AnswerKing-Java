package com.answerdigital.answerking.integration.controller;

import com.answerdigital.answerking.AnswerKingApplication;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;

import com.answerdigital.answerking.response.ProductResponse;
import com.answerdigital.answerking.utility.AbstractContainerBaseTest;
import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {AnswerKingApplication.class, Category.class, Product.class, ProductResponse.class},
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("integration-test")
@ExtendWith({SnapshotExtension.class})
class ProductControllerIntegrationTests extends AbstractContainerBaseTest {

    private Expect expect;

    @Autowired
    private MockMvc mvc;

    @Test
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void getAllProductsTest() throws Exception {

        final var response = mvc.perform(get("/products"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        expect.serializer("json").toMatchSnapshot(response);
    }

}
