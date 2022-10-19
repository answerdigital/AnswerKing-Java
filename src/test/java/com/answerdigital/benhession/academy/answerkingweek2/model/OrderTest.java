package com.answerdigital.benhession.academy.answerkingweek2.model;

import com.answerdigital.benhession.academy.answerkingweek2.dto.AddItemDTO;
import com.answerdigital.benhession.academy.answerkingweek2.dto.AddOrderDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class OrderTest {

    @Test
    void getTotalValueOfBasket_itemsInBasket_correctValue() {
        Order order = new Order(new AddOrderDTO("Home"));
        Map<Item, Integer> items = new HashMap<>();

        Set<Integer> categoryIdSet = new HashSet<>();

        Item burger = new Item(new AddItemDTO("Burger", "A burger", "2.99", true, categoryIdSet));
        burger.setId(1L);
        Item cheeseBurger = new Item(new AddItemDTO("Cheese Burger", "A cheese burger",
                "3.99", true, categoryIdSet));
        cheeseBurger.setId(2L);
        Item chickenBurger = new Item(new AddItemDTO("Chicken Burger", "A chicken burger",
                "2.99", true, categoryIdSet));
        cheeseBurger.setId(3L);

        items.put(burger, 5);
        items.put(cheeseBurger, 2);
        items.put(chickenBurger, 1);

        items.forEach(order::addItemToBasket);

        Assertions.assertEquals(new BigDecimal("25.92"), order.getTotalValueOfBasket());
    }

    @Test
    void getTotalValueOfBasket_emptyBasket_zero() {
        Order order = new Order(new AddOrderDTO("Home"));

        Assertions.assertEquals(new BigDecimal("0"), order.getTotalValueOfBasket());
    }
}
