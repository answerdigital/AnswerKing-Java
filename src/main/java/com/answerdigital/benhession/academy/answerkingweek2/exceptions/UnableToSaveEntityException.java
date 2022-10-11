package com.answerdigital.benhession.academy.answerkingweek2.exceptions;

public class UnableToSaveEntityException extends RuntimeException {
    public UnableToSaveEntityException() {
        super("Unable to save entity");
    }

    public UnableToSaveEntityException(String message) {
        super(message);
    }
}
