package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.builder.LineItemResponseTestBuilder;
import com.answerdigital.answerking.builder.OrderTestBuilder;
import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.repository.OrderRepository;
import com.answerdigital.answerking.request.OrderRequest;
import com.answerdigital.answerking.response.LineItemResponse;
import com.answerdigital.answerking.response.OrderResponse;
import com.answerdigital.answerking.service.OrderService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.answerdigital.answerking.utility.MappingUtility.orderToResponse;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    OrderService orderService;

    @MockBean
    OrderRepository orderRepository;

    private static final OrderTestBuilder ORDER_TEST_BUILDER;

    private static final LineItemResponseTestBuilder LINE_ITEM_RESPONSE_TEST_BUILDER;

    static {
        ORDER_TEST_BUILDER = new OrderTestBuilder();
        LINE_ITEM_RESPONSE_TEST_BUILDER = new LineItemResponseTestBuilder();
    }

    @Test
    void getAllOrdersReturnListOfOrderObjects() throws Exception {
        //given
        final Order order = ORDER_TEST_BUILDER
            .withDefaultValues()
            .build();
        final OrderResponse orderResponse = orderToResponse(order);
        given(orderService.findAll()).willReturn(List.of(orderResponse));

        //when
        final RequestBuilder request = MockMvcRequestBuilders.get("/orders");
        final MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        //then
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode jsonNodeResponse = mapper.readTree(response.getContentAsString());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertFalse(response.getContentAsString().isEmpty());
        assertEquals(orderResponse.getId(), jsonNodeResponse.get(0).get("id").asLong());
        assertTrue(jsonNodeResponse.isArray());
        assertTrue(jsonNodeResponse.get(0).get("lineItems").isArray());
    }

    @Test
    void addOrderTest() throws Exception {
        // given
        final LineItemResponse lineItemResponse = LINE_ITEM_RESPONSE_TEST_BUILDER
            .withDefaultValues()
            .build();
        final Order order = ORDER_TEST_BUILDER
                .withDefaultValues()
                .build();
        final OrderResponse orderResponse = orderToResponse(order);
        orderResponse.getLineItems().add(lineItemResponse);

        //when
        final String orderRequestJson =
                "{\"lineItems\": [{\"productId\": 1,\"quantity\": 5}]}";

        doReturn(orderResponse).when(orderService).addOrder(any(OrderRequest.class));
        final var response = mvc.perform(post("/orders")
                        .content(orderRequestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        //then
        final var resultJsonNode = objectMapper.readTree(response.getContentAsString());

        assertFalse(response.getContentAsString().isEmpty());
        assertEquals(1, resultJsonNode.get("lineItems").get(0).get("product").get("id").asLong());
    }

    @Test
    void getOrderByIdReturnsOkStatusIfExist() throws Exception {
        //given
        final Order order = ORDER_TEST_BUILDER
            .withDefaultValues()
            .build();
        final OrderResponse orderResponse = orderToResponse(order);

        //when
        when(orderService.getOrderResponseById(1L)).thenReturn(orderResponse);

        final ResultActions actualPerformResult = mvc.perform(get("/orders/{orderId}", 1L))
                .andExpect(status().isOk());
        final ObjectMapper mapper = new ObjectMapper();

        //then
        assertEquals(orderResponse.getId(), mapper.readTree(actualPerformResult.andReturn()
                .getResponse().getContentAsString()).get("id").asLong());
    }

    @Test
    void updateOrderTest() throws Exception {
        // given
        final LineItemResponse lineItemResponse = LINE_ITEM_RESPONSE_TEST_BUILDER
            .withDefaultValues()
            .build();
        final Order order = ORDER_TEST_BUILDER
                .withDefaultValues()
                .build();
        final OrderResponse orderResponse = orderToResponse(order);
        orderResponse.getLineItems().add(lineItemResponse);

        //when
        final String orderRequestJson =
                "{\"lineItems\": [{\"productId\": 1,\"quantity\": 1}]}";

        doReturn(orderResponse).when(orderService).updateOrder(anyLong(),any(OrderRequest.class));
        final var response = mvc.perform(put("/orders/{orderId}", 1L)
                        .content(orderRequestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        //then
        final JsonNode responseLineItems = objectMapper.readTree(response.getContentAsString()).get("lineItems");

        assertTrue(responseLineItems.isArray());
        assertEquals(1, responseLineItems.get(0).get("product").get("id").asLong());
    }

    @Test
    void cancelOrderReturnsNoContent() throws Exception {
        mvc.perform(delete("/orders/{orderId}", 1L))
                .andExpect(status().isNoContent());
    }
}
