package com.answerdigital.answerking;

import com.answerdigital.answerking.utility.AbstractContainerBaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AnswerKingApplicationTest extends AbstractContainerBaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllProductsReturnListOfProductObjects() throws Exception {
        //when
        final ObjectMapper mapper = new ObjectMapper();
        final RequestBuilder request = MockMvcRequestBuilders.get("/products");
        final MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertFalse(response.getContentAsString().isEmpty());

        assertEquals("Burger", mapper.readTree(response.getContentAsString()).get(0).get("name").textValue());
        assertEquals("300g Beef", mapper.readTree(response.getContentAsString()).get(0).get("description").textValue());
        assertEquals(6.69, mapper.readTree(response.getContentAsString()).get(0).get("price").asDouble());
    }
}
