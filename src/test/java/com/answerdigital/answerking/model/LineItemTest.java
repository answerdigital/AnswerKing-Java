package com.answerdigital.answerking.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class LineItemTest {

    @Test
    public void testEqualsWhenMatch() {
        final LineItem lineItem = new LineItem();
        assertEquals(lineItem, lineItem);
        assertEquals(lineItem.hashCode(), lineItem.hashCode());
    }

    @Test
    public void testEqualsWhenIdDoNotMatch() {
        final LineItem lineItem = new LineItem();
        lineItem.setId(1L);
        final LineItem lineItem1 = new LineItem();
        lineItem1.setId(2L);
        assertEquals(lineItem, lineItem1);
        assertNotEquals(lineItem.getId(), lineItem1.getId());
        assertEquals(lineItem.hashCode(), lineItem1.hashCode());
    }

    @Test
    public void testNotEqualsWhenQuantityDoNotMatch() {
        final Order order = new Order();
        final LineItem lineItem = new LineItem(order, new Product(), 1);
        final LineItem lineItem1 = new LineItem(order, new Product(), 2);
        assertNotEquals(lineItem, lineItem1);
    }

    @Test
    public void testToStringMethod() {
        final Order order = new Order();
        order.setId(5L);
        final Product product = new Product();
        product.setId(77L);
        final LineItem lineItem = new LineItem(order, product, 45);
        lineItem.setId(22L);
        assertEquals("LineItem{id=22, orderId=5, productId=77, quantity=45}",
                lineItem.toString());
    }
}

