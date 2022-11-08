package com.answerdigital.academy.answerking.service;

import com.answerdigital.academy.answerking.exception.generic.ConflictException;
import com.answerdigital.academy.answerking.exception.generic.NotFoundException;
import com.answerdigital.academy.answerking.model.Item;
import com.answerdigital.academy.answerking.repository.ItemRepository;
import com.answerdigital.academy.answerking.request.ItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemService itemService;
    private Item item;
    private ItemRequest itemRequest;

    @BeforeEach
    public void generateItem() {
        item = Item.builder()
                .id(55L)
                .name("test")
                .description("testDes")
                .price(BigDecimal.valueOf(2.99))
                .available(true)
                .build();
        itemRequest = ItemRequest.builder()
                .name("test")
                .description("testD")
                .price(BigDecimal.valueOf(1.99))
                .available(true)
                .build();
    }

    @Test
    void addNewItemReturnsItemObjectSuccessfully() {
        //given
        when(itemRepository.save(any())).thenReturn(item);
        when(itemRepository.existsByName(any())).thenReturn(false);
        //when
        Item actualAddNewItemResult = itemService.addNewItem(
                itemRequest);
        //then
        assertEquals(item.getName(), actualAddNewItemResult.getName());
        assertEquals(item.getPrice().toString(), actualAddNewItemResult.getPrice().toString());
        verify(itemRepository).save(any());
    }

    @Test
    void addNewItemThrowsExceptionIfItemNameAlreadyExist() {
        //given
        when(itemRepository.existsByName(any())).thenReturn(true);
        //when
        assertThatThrownBy(() -> itemService.addNewItem(itemRequest))
                //then
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void getItemByIdReturnItemObject() {
        //given
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        //when
        Item actualAddNewItemResult = itemService.findById(12L);
        //then
        assertEquals(item.getName(), actualAddNewItemResult.getName());
        assertEquals(item.getPrice().toString(), actualAddNewItemResult.getPrice().toString());
        verify(itemRepository).findById(any());
    }

    @Test
    void getItemByIdReturnsNotFoundExceptionIfItemIdDoesNotExist() {
        //when
        assertThatThrownBy(() -> itemService.findById(15L))
                //then
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("does not exist");
    }

    @Test
    void getAllItemsReturnListOfItemObjects() {
        //given
        when(itemRepository.findAll()).thenReturn(List.of(item));
        //when
        List<Item> actualResult = itemService.findAll();
        //then
        assertFalse(actualResult.isEmpty());
        assertEquals(actualResult.get(0).getName(), item.getName());
        verify(itemRepository).findAll();
    }

    @Test
    void updateItemReturnsItemObjectSuccessfully() {
        //given
        when(itemRepository.save(any())).thenReturn(item);
        when(itemRepository.existsByNameAndIdIsNot(any(), anyLong())).thenReturn(false);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

        //when
        Item actualAddNewItemResult = itemService.updateItem(12L,
                itemRequest);

        //then
        assertEquals(item.getName(), actualAddNewItemResult.getName());
        assertEquals(item.getPrice().toString(), actualAddNewItemResult.getPrice().toString());
        verify(itemRepository).save(any());
    }

    @Test
    void updateItemThrowsExceptionIfItemIdDoesNotExist() {
        //when
        assertThatThrownBy(() -> itemService.updateItem(12L, itemRequest))
                //then
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("does not exist");
    }

    @Test
    void updateItemThrowsExceptionIfItemNameAlreadyExist() {
        //given
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(itemRepository.existsByNameAndIdIsNot(any(), anyLong())).thenReturn(true);
        //when
        assertThatThrownBy(() -> itemService.updateItem(12L, itemRequest))
                //then
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already exists");
    }
}

