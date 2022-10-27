package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.ConflictException;
import com.answerdigital.benhession.academy.answerkingweek2.exceptions.NotFoundException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.ItemRepository;
import com.answerdigital.benhession.academy.answerkingweek2.request.ItemRequest;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemService itemService;
    private Item item;

    @BeforeEach
    public void generateItem() {
        item = new Item("test", "testDes", new BigDecimal("2.99"), true);
        item.setId(55L);
    }

    @Test
    void addNewItemReturnsItemObjectSuccessfully() {
        //given
        when(itemRepository.save(any())).thenReturn(item);
        when(itemRepository.existsByName(any())).thenReturn(false);
        //when
        Item actualAddNewItemResult = itemService.addNewItem(
                new ItemRequest("test", "testD", BigDecimal.valueOf(1.99), true));
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
        ItemRequest itemRequest = new ItemRequest("test", "testD", BigDecimal.valueOf(1.99), true);
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
                new ItemRequest("test", "testD", BigDecimal.valueOf(1.99), true));

        //then
        assertEquals(item.getName(), actualAddNewItemResult.getName());
        assertEquals(item.getPrice().toString(), actualAddNewItemResult.getPrice().toString());
        verify(itemRepository).save(any());
    }

    @Test
    void updateItemThrowsExceptionIfItemIdDoesNotExist() {
        //given
        ItemRequest itemRequest = new ItemRequest("test", "testD", BigDecimal.valueOf(1.99), true);
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
        ItemRequest itemRequest = new ItemRequest("test", "testD", BigDecimal.valueOf(1.99), true);
        //when
        assertThatThrownBy(() -> itemService.updateItem(12L, itemRequest))
                //then
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already exists");
    }
}
