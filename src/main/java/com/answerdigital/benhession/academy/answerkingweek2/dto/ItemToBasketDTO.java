package com.answerdigital.benhession.academy.answerkingweek2.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ItemToBasketDTO {

    @NotNull
    private final Integer itemId;
    @NotNull
    @Min(value = 1, message = "quantity must be greater than 0")
    private final Integer quantity;

    public ItemToBasketDTO(Integer itemId, Integer quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "ItemToBasketDTO{" +
                "id=" + itemId +
                ", quantity=" + quantity +
                '}';
    }
}
