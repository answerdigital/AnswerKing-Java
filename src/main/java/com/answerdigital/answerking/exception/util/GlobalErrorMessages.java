package com.answerdigital.answerking.exception.util;

public final class GlobalErrorMessages {
    private GlobalErrorMessages() { }

    // PRODUCTS
    public static final String PRODUCTS_DO_NOT_EXIST = "One or more Products do not exist, with ID(s) or name(s) [%s].";

    public static final String PRODUCTS_ARE_RETIRED = "One or more Products are already retired, with ID(s) or name(s) [%s].";

    public static final String PRODUCTS_ALREADY_EXIST = "One or more Products already exist, with ID(s) or name(s) [%s].";

    // ORDERS
    public static final String ORDERS_DO_NOT_EXIST = "One or more Orders do not exist, with ID(s) or name(s) [%s].";

    public static final String ORDERS_ALREADY_CANCELLED = "One or more Orders are already cancelled, with ID(s) or name(s)" +
        " [%s].";

    // CATEGORIES
    public static final String CATEGORIES_DO_NOT_EXIST = "One or more Categories do not exist, with ID(s) or name(s) [%s].";

    public static final String CATEGORIES_ALREADY_EXIST = "One or more Categories already exist, with ID(s) or name(s) [%s].";

    public static final String CATEGORIES_ARE_RETIRED = "One or more Categories are already retired, with ID(s) or name(s) [%s].";

    public static final String CATEGORIES_PRODUCTS_ALREADY_PRESENT = "One or more Products are already present within " +
        "this Category, with ID(s) or name(s) [%s].";

    public static final String CATEGORIES_PRODUCTS_NOT_FOUND = "One or more Products could not be found within this Category, " +
        "with ID(s) or name(s) [%s]";
}
