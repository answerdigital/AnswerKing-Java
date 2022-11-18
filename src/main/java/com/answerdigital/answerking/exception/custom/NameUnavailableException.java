package com.answerdigital.answerking.exception.custom;

import com.answerdigital.answerking.exception.generic.BadRequestException;

import java.util.Collection;

public class NameUnavailableException extends BadRequestException {

    public NameUnavailableException(final String error) {
        super(error);
    }
}
