package com.answerdigital.answerking.exception.util;

import com.answerdigital.answerking.exception.AnswerKingException;
import com.answerdigital.answerking.exception.custom.NameUnavailableException;
import com.answerdigital.answerking.exception.custom.OrderCancelledException;
import com.answerdigital.answerking.exception.custom.ProductAlreadyPresentException;
import com.answerdigital.answerking.exception.custom.RetirementException;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;

@Getter
public enum GlobalErrorMessage {
    // PRODUCT
    PRODUCTS_DO_NOT_EXIST("One or more Products do not exist with ID(s) or name(s) [%s].",
            NotFoundException.class),
    PRODUCTS_ARE_RETIRED("One or more Products are already retired with ID(s) or name(s) [%s].",
            RetirementException.class),
    PRODUCTS_ALREADY_EXIST("One or more Products already exist with ID(s) or name(s) [%s].",
            NameUnavailableException.class),

    // ORDER
    ORDERS_DO_NOT_EXIST("One or more Orders do not exist with ID(s) or name(s) [%s].",
            NotFoundException.class),
    ORDERS_ALREADY_CANCELLED("One or more Orders are already cancelled with ID(s) or name(s) [%s].",
            OrderCancelledException.class),

    // CATEGORY
    CATEGORIES_DO_NOT_EXIST("One or more Categories do not exist with ID(s) or name(s) [%s].",
            NotFoundException.class),
    CATEGORIES_ALREADY_EXIST("One or more Categories already exist with ID(s) or name(s) [%s].",
            NameUnavailableException.class),
    CATEGORIES_ARE_RETIRED("One or more Categories are already retired with ID(s) or name(s) [%s].",
            RetirementException.class),
    CATEGORIES_PRODUCTS_ALREADY_PRESENT(
            "One or more Products are already present within this Category with ID(s) or name(s) [%s].",
            ProductAlreadyPresentException.class),
    CATEGORIES_PRODUCTS_NOT_FOUND(
            "One or more Products could not be found within this Category with ID(s) or name(s) [%s].",
            NotFoundException.class),

    // TAG
    TAGS_ALREADY_EXIST("One or more Tags already exist with ID(s) or name(s) [%s].",
            NameUnavailableException.class),
    TAGS_DO_NOT_EXIST("One or more Tags could not be found with ID(s) or name(s) [%s].",
            NotFoundException.class),
    TAGS_ARE_RETIRED("One or more Tags are already retired with ID(s) or name(s) [%s].",
            RetirementException.class);

    final String errorMessageString;
    final Class<? extends AnswerKingException> exception;

    GlobalErrorMessage(
            final String errorMessageString,
            final Class<? extends AnswerKingException> exception
    ) {
        this.errorMessageString = errorMessageString;
        this.exception = exception;
    }

    @SneakyThrows
    public static AnswerKingException getCustomException(
            final GlobalErrorMessage globalErrorMessage,
            final Object... messageArgs
    ) {
        final Constructor<? extends AnswerKingException> exceptionConstructor =
                globalErrorMessage.exception.getConstructor(String.class, String.class);

        final String errorMessageId = globalErrorMessage.name();
        final String errorMessage = String.format(globalErrorMessage.errorMessageString, messageArgs);

        return exceptionConstructor.newInstance(errorMessageId, errorMessage);
    }
}
