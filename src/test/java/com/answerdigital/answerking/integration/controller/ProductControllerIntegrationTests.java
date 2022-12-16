package com.answerdigital.answerking.integration.controller;

import com.answerdigital.answerking.AnswerKingApplication;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.response.ProductResponse;
import com.answerdigital.answerking.utility.AbstractContainerBaseTest;
import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {AnswerKingApplication.class, Category.class, Product.class, ProductResponse.class},
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("integration-test")
@ExtendWith({SnapshotExtension.class})
class ProductControllerIntegrationTests extends AbstractContainerBaseTest {

    private Expect expect;
    public String uuidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

    @Autowired
    private MockMvc mvc;

    @Test
    @Tag("GET")
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void getAllProductsTest() throws Exception {

        final var response = mvc.perform(get("/products"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        expect.serializer("json").toMatchSnapshot(response);
    }

    @Test
    @Tag("GET")
    @Sql({"/test_sql_scripts/clearAll4Test.sql"})
    void getAllProductsEmptyTest() throws Exception {

        final var response = mvc.perform(get("/products"))
            .andExpect(status().isNoContent())
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThat(response).isEqualTo("[]");
    }

    @Test
    @Tag("GET")
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void getProductByIDTest() throws Exception {

        final var response = mvc.perform(get("/products/1")) // Need to get ID from seed really
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        expect.serializer("json").toMatchSnapshot(response);
    }

    @Test
    @Tag("GET")
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void getProductByIDNotFoundTest() throws Exception {

        final var response = mvc.perform(get("/products/10000")) 
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String sanitisedResponse = response.replaceAll(uuidRegex, "{ Sanitised }");
        expect.serializer("json").toMatchSnapshot(sanitisedResponse); // This could be handled much better if the json was serialised. Then we could point to the json field and sanatise using that.
    }

    @Test
    @Tag("GET")
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void getProductByIDInvalidTest() throws Exception {

        final var response = mvc.perform(get("/products/invalid")) 
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String sanitisedResponse = response.replaceAll(uuidRegex, "{ Sanitised }");
        expect.serializer("json").toMatchSnapshot(sanitisedResponse); // This could be handled much better if the json was serialised. Then we could point to the json field and sanatise using that.
    }

    @Test
    @Tag("POST")
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void postProductTest() throws Exception {

        final var response = mvc.perform(post("/products")
            .contentType("application/json")
            .content("{\"name\": \"Kabab\", \"description\": \"Product 1 desc\", \"price\": 10.34, \"categoryId\": 1}")) 
            // Need to add static fixtures instead of writing this in tests
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        expect.serializer("json").toMatchSnapshot(response); 
    }

    @Test
    @Tag("POST")
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void postProductTestInvalidBody() throws Exception {

        final var response = mvc.perform(post("/products")
            .contentType("application/json")
            .content("This is not valid JSON")) 
            // Need to add static fixtures and be data driven to check all different ways body can be incorrect
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String sanitisedResponse = response.replaceAll(uuidRegex, "{ Sanitised }");
        expect.serializer("json").toMatchSnapshot(sanitisedResponse); // This could be handled much better if the json was serialised. Then we could point to the json field and sanatise using that.
    }

    @Test
    @Tag("PUT")
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void putProductTest() throws Exception {

        final var response = mvc.perform(put("/products/2")
            .contentType("application/json")
            .content("{\"name\": \"Kabab\", \"description\": \"Product 1 desc\", \"price\": 10.34, \"categoryId\": 1}")) 
            // Need to add static fixtures instead of writing this in tests
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        expect.serializer("json").toMatchSnapshot(response); 
    }

    @Test
    @Tag("PUT")
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void putProductTestInvalidBody() throws Exception {

        final var response = mvc.perform(put("/products/2")
            .contentType("application/json")
            .content("This is not valid JSON")) 
            // Need to add static fixtures and be data driven to check all different ways body can be incorrect
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

            String sanitisedResponse = response.replaceAll(uuidRegex, "{ Sanitised }");
            expect.serializer("json").toMatchSnapshot(sanitisedResponse); // This could be handled much better if the json was serialised. Then we could point to the json field and sanatise using that.
    }

    @Test
    @Tag("PUT")
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void putProductByIDInvalidTest() throws Exception {

        final var response = mvc.perform(put("/products/invalid")) 
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String sanitisedResponse = response.replaceAll(uuidRegex, "{ Sanitised }");
        expect.serializer("json").toMatchSnapshot(sanitisedResponse); // This could be handled much better if the json was serialised. Then we could point to the json field and sanatise using that.
    }

    @Test
    @Tag("DELETE")
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void deleteProductByIDTest() throws Exception {

        mvc.perform(delete("/products/2")) // Need to get ID from seed really
            .andExpect(status().isNoContent())
            .andReturn()
            .getResponse()
            .getContentAsString();
        // Add another assertion that checks the db and makes sure retired is set to true
    }

    @Test
    @Tag("DELETE")
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void deleteProductByIDNotFoundTest() throws Exception {

        final var response = mvc.perform(delete("/products/10000")) 
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String sanitisedResponse = response.replaceAll(uuidRegex, "{ Sanitised }");
        expect.serializer("json").toMatchSnapshot(sanitisedResponse); // This could be handled much better if the json was serialised. Then we could point to the json field and sanatise using that.
    }

    @Test
    @Tag("DELETE")
    @Sql({"/test_sql_scripts/clearAll4Test.sql", "/test_sql_scripts/product_controller_test_input.sql"})
    void deleteProductByIDInvalidTest() throws Exception {

        final var response = mvc.perform(delete("/products/invalid")) 
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String sanitisedResponse = response.replaceAll(uuidRegex, "{ Sanitised }");
        expect.serializer("json").toMatchSnapshot(sanitisedResponse); // This could be handled much better if the json was serialised. Then we could point to the json field and sanatise using that.
    }
}
