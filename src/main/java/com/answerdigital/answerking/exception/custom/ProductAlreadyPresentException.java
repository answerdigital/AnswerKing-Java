package com.answerdigital.answerking.exception.custom;

import com.answerdigital.answerking.exception.generic.BadRequestException;

public class ProductAlreadyPresentException extends BadRequestException {

    public ProductAlreadyPresentException(final String error) {
        super(error);
    }
}
