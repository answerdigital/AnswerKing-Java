package com.answerdigital.answerking.exception.custom;

import com.answerdigital.answerking.exception.generic.BadRequestException;

/**
 * The class NameUnavailableException represents an exception when a requested name
 * cannot be found on the database.
 */
public class NameUnavailableException extends BadRequestException {

    public NameUnavailableException(final String errorMessageId, final String errorMessage) {
        super(errorMessageId, errorMessage);
    }
}
