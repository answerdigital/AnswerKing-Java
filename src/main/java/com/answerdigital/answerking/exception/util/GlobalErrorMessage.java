package com.answerdigital.answerking.exception.util;

public enum GlobalErrorMessage {
    // PRODUCT
    PRODUCTS_DO_NOT_EXIST("One or more Products do not exist with ID(s) or name(s) [%s].", "PRODUCT1"),
    PRODUCTS_ARE_RETIRED("One or more Products are already retired with ID(s) or name(s) [%s].", "PRODUCT2"),
    PRODUCTS_ALREADY_EXIST("One or more Products already exist with ID(s) or name(s) [%s].", "PRODUCT3"),

    // ORDER
    ORDERS_DO_NOT_EXIST("One or more Orders do not exist with ID(s) or name(s) [%s].", "ORDER1"),
    ORDERS_ALREADY_CANCELLED("One or more Orders are already cancelled with ID(s) or name(s) [%s].", "ORDER2"),

    // CATEGORY
    CATEGORIES_DO_NOT_EXIST("One or more Categories do not exist with ID(s) or name(s) [%s].", "CATEGORY1"),
    CATEGORIES_ALREADY_EXIST("One or more Categories already exist with ID(s) or name(s) [%s].", "CATEGORY2"),
    CATEGORIES_ARE_RETIRED("One or more Categories are already retired with ID(s) or name(s) [%s].", "CATEGORY3"),
    CATEGORIES_PRODUCTS_ALREADY_PRESENT(
            "One or more Products are already present within this Category with ID(s) or name(s) [%s].",
            "CATEGORY4"),
    CATEGORIES_PRODUCTS_NOT_FOUND(
            "One or more Products could not be found within this Category with ID(s) or name(s) [%s]",
            "CATEGORY5");

    final String errorMessageString;
    final String errorMessageId;

    GlobalErrorMessage(final String errorMessageString, final String errorMessageId) {
        this.errorMessageString = errorMessageString;
        this.errorMessageId = errorMessageId;
    }

    public static String getCustomExceptionMessage(
            final GlobalErrorMessage globalErrorMessage,
            final Object... messageArgs
    ) {
        return String.format(globalErrorMessage.errorMessageString, messageArgs)
                + " ERROR CODE - " + globalErrorMessage.errorMessageId;
    }
}
