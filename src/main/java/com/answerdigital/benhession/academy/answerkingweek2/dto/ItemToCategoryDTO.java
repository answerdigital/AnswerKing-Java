package com.answerdigital.benhession.academy.answerkingweek2.dto;

import javax.validation.constraints.NotNull;

public class ItemToCategoryDTO {

    @NotNull
    private Integer itemId;

    public ItemToCategoryDTO(Integer id) {
        this.itemId = id;
    }

    public ItemToCategoryDTO() {
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "ItemToCategoryDTO{" +
                "itemId=" + itemId +
                '}';
    }
}
