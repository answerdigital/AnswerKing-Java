package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.dto.AddOrderDTO;
import com.answerdigital.benhession.academy.answerkingweek2.dto.GetOrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MimeTypeUtils;

@ActiveProfiles(value = "test")
@SpringBootTest
@Sql(scripts = {"/schema.sql", "/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/post-test-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureMockMvc
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;
    private final Logger logger = LoggerFactory.getLogger(OrderControllerIT.class);

    @Test
    void smokeTest() {
        assert true;
    }

    private String getJsonString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

    @Test
    void addOrder_validDTO_createdOrderReturned() throws Exception  {

        AddOrderDTO addOrderDTO = new AddOrderDTO("Union mills");

        try {
            String requestBody = getJsonString(addOrderDTO);

            mockMvc.perform(MockMvcRequestBuilders
                .post("/order")
                .contentType(MimeTypeUtils.APPLICATION_JSON_VALUE)
                .content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andDo(result -> {
                String responseString = result.getResponse().getContentAsString();
                ObjectMapper objectMapper = new ObjectMapper();

                GetOrderDTO getOrderDTO = objectMapper.readValue(responseString, GetOrderDTO.class);

                System.out.println();

                Assertions.assertThat(getOrderDTO.getAddress()).isEqualTo(addOrderDTO.getAddress());
            });
        } catch (JsonProcessingException e) {
            logger.error(e::getMessage);
            assert false;
        }
    }

    @Test
    void addOrder_invalidDTO_httpStatusBadRequest() throws Exception {
        AddOrderDTO addOrderDTO = new AddOrderDTO("");
        try {
            String requestBody = getJsonString(addOrderDTO);

            mockMvc.perform(MockMvcRequestBuilders
                    .post("/order")
                    .contentType(MimeTypeUtils.APPLICATION_JSON_VALUE)
                    .content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        } catch (JsonProcessingException e) {
            logger.error(e::getMessage);
            assert false;
        }
    }
}
