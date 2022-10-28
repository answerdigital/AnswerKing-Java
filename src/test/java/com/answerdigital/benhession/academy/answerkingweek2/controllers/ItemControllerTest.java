package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.services.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private ItemController itemController;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private Item item;

    @BeforeEach
    public void generateItem() {
        item = Item.builder()
                .id(55L)
                .name("test")
                .description("testDes")
                .price(BigDecimal.valueOf(2.99))
                .available(true)
                .build();
    }

    @WithMockUser("paul")
    @Test
    void getAllItemsReturnListOfItemObjects() throws Exception {
        //given
        given(itemService.findAll()).willReturn(List.of(item));

        //when
        RequestBuilder request = MockMvcRequestBuilders.get("/item");

        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertFalse(response.getContentAsString().isEmpty());
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(item.getId(), mapper.readTree(response.getContentAsString()).get(0).get("id").asLong());
    }

    @WithMockUser("paul")
    @Test
    void getAllItemsReturnNoContentIfEmpty() throws Exception {
        //when
        mvc.perform(get("/item"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser("paul")
    @Test
    void getItemByIdReturnsOkStatusIfExist() throws Exception {
        //given
        when(itemService.findById(55L)).thenReturn(item);
        //when
        ResultActions actualPerformResult = mvc.perform(get("/item/{id}", 55L)).andExpect(status().isOk());
        ObjectMapper mapper = new ObjectMapper();
        //then
        assertEquals(item.getId(), mapper.readTree(actualPerformResult.andReturn()
                .getResponse().getContentAsString()).get("id").asLong());
    }

    @WithMockUser("paul")
    @Test
    void getItemByIdReturnsNotFoundExceptionIfIdDoesNotExist() throws Exception {
        mvc.perform(get("/{id}", 55L)).andExpect(status().isNotFound());
    }

    @WithMockUser("paul")
    @Test
    void addItemWithInvalidNameThrowsException() throws Exception {
        //given
        String newItem = "{\"name\": \"abc12\",\"description\": \"descTest\",\"price\": \"4.75\",\"available\": \"true\"}";
        //when
        mvc.perform(post("/item")
                        .content(newItem)
                        .contentType(MediaType.APPLICATION_JSON))
        //then
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("[Item name must only contain letters, spaces and dashes]")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @WithMockUser("paul")
    @Test
    void addItemWithValidObjectReturnsItemAndOkStatus() throws Exception {
        //given
        String newItem = "{\"name\": \"test\",\"description\": \"descTest\",\"price\": \"4.75\",\"available\": \"true\"}";
        given(itemService.addNewItem(any())).willReturn(item);

        //when
        ResultActions actualPerformResult = mvc.perform(post("/item")
                .content(newItem)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        actualPerformResult.andExpect(status().isOk());
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(mapper.readTree(newItem).get("name"), mapper.readTree(actualPerformResult.andReturn()
                .getResponse().getContentAsString()).get("name"));
    }

    @WithMockUser("paul")
    @Test
    void updateItemWithInvalidPriceThrowsException() throws Exception {
        //given
        String newItem = "{\"name\": \"abc\",\"description\": \"descTest\",\"price\": \"4.7587\",\"available\": \"true\"}";
        //when
        mvc.perform(MockMvcRequestBuilders.put("/item/{id}", 55L)
                        .content(newItem)
                        .contentType(MediaType.APPLICATION_JSON))
        //then
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("[Item price is invalid]")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @WithMockUser("paul")
    @Test
    void updateItemWithValidObjectReturnsItemAndOkStatus() throws Exception {
        //given
        String newItem = "{\"name\": \"test\",\"description\": \"descTest\",\"price\": \"4.75\",\"available\": \"true\"}";
        given(itemService.updateItem(eq(55L), any())).willReturn(item);
        //when

        ResultActions actualPerformResult = mvc.perform(put("/item/{id}", 55L)
                .content(newItem)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actualPerformResult.andExpect(status().isOk());
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(mapper.readTree(newItem).get("name"), mapper.readTree(actualPerformResult.andReturn()
                .getResponse().getContentAsString()).get("name"));
    }
}

