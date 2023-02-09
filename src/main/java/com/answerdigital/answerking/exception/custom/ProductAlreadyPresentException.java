package com.answerdigital.answerking.exception.custom;

import com.answerdigital.answerking.exception.generic.BadRequestException;

/**
 * The class ProductAlreadyPresentException represents an exception when a
 * Product {@link com.answerdigital.answerking.model.Product} is already present.
 */
public class ProductAlreadyPresentException extends BadRequestException {

    public ProductAlreadyPresentException(final String errorMessageId, final String errorMessage) {
        super(errorMessageId, errorMessage);
    }
}
