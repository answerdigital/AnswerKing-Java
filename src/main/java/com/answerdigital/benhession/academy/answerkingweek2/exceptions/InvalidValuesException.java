package com.answerdigital.benhession.academy.answerkingweek2.exceptions;

public class InvalidValuesException extends RuntimeException {
    public InvalidValuesException() {
        super("The values entered for the item do not meet validation requirements.");
    }

    public InvalidValuesException(String message) {
        super(message);
    }
}
