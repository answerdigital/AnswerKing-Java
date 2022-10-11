package com.answerdigital.benhession.academy.answerkingweek2.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    static Logger logger = LoggerFactory.getLogger("ResponseEntityExceptionHandler");

    static public void checkDTOFields(BindingResult bindingResult) throws InvalidValuesException {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(fieldError -> {
                if (Objects.equals(fieldError.getCode(), "NotBlank") ||
                        Objects.equals(fieldError.getCode(), "NotNull") ||
                        Objects.equals(fieldError.getCode(), "Min")) {
                    throw new InvalidValuesException(
                            "Ensure all required fields are present in the body of the request " +
                                    "and have the correct value.");
                } else {
                    logger.error(fieldError.toString());
                    throw new InvalidValuesException("The binding result contains an unknown error");
                }
            });
        }
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleItemNotFound(RuntimeException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(InvalidValuesException.class)
    public ResponseEntity<ErrorResponse> handleInvalidItemDTO(RuntimeException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictingItems(RuntimeException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request);

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(UnableToSaveEntityException.class)
    public ResponseEntity<ErrorResponse> handleUnableToSave(RuntimeException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request);

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(ItemUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleItemUnavailable(RuntimeException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(HttpStatus.FORBIDDEN, e.getMessage(), request);

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

}
