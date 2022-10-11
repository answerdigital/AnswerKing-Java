package com.answerdigital.benhession.academy.answerkingweek2.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ItemQuantityDTO {

    @NotNull
    @Min(value = 1,  message = "quantity must be greater than 0")
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }
}
