package com.answerdigital.benhession.academy.answerkingweek2.dto;

import javax.validation.constraints.NotBlank;

public class AddOrderDTO {

    @NotBlank
    private String address;

    public AddOrderDTO() {
    }

    public AddOrderDTO(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
