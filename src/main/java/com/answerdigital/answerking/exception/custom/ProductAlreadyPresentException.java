package com.answerdigital.answerking.exception.custom;

import com.answerdigital.answerking.exception.generic.BadRequestException;

import java.util.Collection;

public class ProductAlreadyPresentException extends BadRequestException {

    public ProductAlreadyPresentException(final String error) {
        super(error);
    }
}
