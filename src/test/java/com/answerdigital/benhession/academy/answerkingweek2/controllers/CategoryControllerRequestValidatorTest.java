package com.answerdigital.benhession.academy.answerkingweek2.controllers;

import com.answerdigital.benhession.academy.answerkingweek2.request.AddCategoryRequest;
import com.answerdigital.benhession.academy.answerkingweek2.request.UpdateCategoryRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

class CategoryControllerRequestValidatorTest {

    private Validator validator;

    private static final String DEFAULT_NAME = "Drinks";
    private static final String DEFAULT_DESCRIPTION = "Our selection of drinks";

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
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
    void addCategoryWithInvalidRequest(String name, String description) {
        // given
        AddCategoryRequest addCategoryRequest = new AddCategoryRequest(name, description);

        // when
        Set<ConstraintViolation<AddCategoryRequest>> violations = validator.validate(addCategoryRequest);

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
    void updateCategoryWithInvalidRequest(String name, String description) {
        // given
        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(name, description);

        // when
        Set<ConstraintViolation<UpdateCategoryRequest>> violations = validator.validate(updateCategoryRequest);

        // then
        assertFalse(violations.isEmpty());
    }
}

