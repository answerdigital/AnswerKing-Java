package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.request.CategoryRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CategoryControllerRequestValidatorTest {

    private Validator validator;

    private static final String DEFAULT_NAME = "Drinks";

    private static final String DEFAULT_DESCRIPTION = "Our selection of drinks";

    @BeforeEach
    void setUp() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    void tearDown() {
        validator = null;
    }

    private static Stream<Arguments> provideInvalidStringsForAddCategory() {
        // null, blank and invalid format for both name & description
        return Stream.of(
                Arguments.of(null, DEFAULT_DESCRIPTION),
                Arguments.of("", DEFAULT_DESCRIPTION),
                Arguments.of("$^&*$%^", DEFAULT_DESCRIPTION),
                Arguments.of(DEFAULT_NAME, null),
                Arguments.of(DEFAULT_NAME, ""),
                Arguments.of(DEFAULT_NAME, "$^&*$%^")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidStringsForAddCategory")
    void addCategoryWithInvalidRequest(final String name, final String description) {
        // given

        final CategoryRequest addCategoryRequest = new CategoryRequest(name, description);

        // when
        final Set<ConstraintViolation<CategoryRequest>> violations = validator.validate(addCategoryRequest);

        // then
        assertFalse(violations.isEmpty());
    }

    private static Stream<Arguments> provideInvalidStringsForUpdateCategory() {
        // null, blank and invalid format for both name & description
        return Stream.of(
                Arguments.of(null, DEFAULT_DESCRIPTION),
                Arguments.of("", DEFAULT_DESCRIPTION),
                Arguments.of("$^&*$%^", DEFAULT_DESCRIPTION),
                Arguments.of(DEFAULT_NAME, null),
                Arguments.of(DEFAULT_NAME, ""),
                Arguments.of(DEFAULT_NAME, "$^&*$%^")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidStringsForUpdateCategory")
    void updateCategoryWithInvalidRequest(final String name, final String description) {
        // given
        final CategoryRequest updateCategoryRequest = new CategoryRequest(name, description);

        // when
        final Set<ConstraintViolation<CategoryRequest>> violations = validator.validate(updateCategoryRequest);

        // then
        assertFalse(violations.isEmpty());
    }
}

