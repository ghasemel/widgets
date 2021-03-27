package com.elyasi.assignments.widgets.exception.handler;

import com.elyasi.assignments.widgets.constant.ErrorConstant;
import com.elyasi.assignments.widgets.dto.ExceptionDto;
import com.elyasi.assignments.widgets.exception.defined.WidgetNotFoundException;
import com.elyasi.assignments.widgets.exception.defined.bad.BadRequestException;
import com.elyasi.assignments.widgets.exception.defined.bad.InvalidRequestBodyException;
import com.elyasi.assignments.widgets.exception.defined.bad.MutabilityException;
import com.elyasi.assignments.widgets.exception.defined.bad.InvalidValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ghasem on 27/03/2021
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class DefinedTypeExceptionHandler {

    @ExceptionHandler({
            InvalidValueException.class,
            InvalidRequestBodyException.class,
            MutabilityException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleBadRequestException(BadRequestException exc) {
        log.error(ErrorConstant.INVALID_VALUE, exc);

        return ExceptionDto.builder()
                .message(exc.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .build();
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exc, Errors errors) {
        log.error(ErrorConstant.INVALID_VALUE, exc);

        List<String> messages = new ArrayList<>();
        if (errors != null && errors.hasErrors()) {
            for (FieldError error : errors.getFieldErrors()) {
                messages.add(error.getDefaultMessage());
            }
        }

        return ExceptionDto.builder()
                .messages(messages)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .build();
    }

    @ExceptionHandler({
            ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleConstraintViolationException(ConstraintViolationException exc) {
        log.error(ErrorConstant.INVALID_VALUE, exc);

        return ExceptionDto.builder()
                .message(exc.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .build();
    }


    @ExceptionHandler(WidgetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleWidgetNotFoundException(WidgetNotFoundException exc) {
        log.error(ErrorConstant.NOT_FOUND, exc);

        return ExceptionDto.builder()
                .message(exc.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .build();
    }
}
