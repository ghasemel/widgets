package com.elyasi.assignments.widgets.exception.handler;

import com.elyasi.assignments.widgets.constant.ErrorConstant;
import com.elyasi.assignments.widgets.dto.ExceptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.constraints.NotNull;


/**
 * Created by Ghasem on 27/03/2021
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ExceptionDto handleUnknownException(@NotNull Exception exc) {
        log.error(ErrorConstant.INTERNAL_ERROR, exc);

        return ExceptionDto.builder().message(ErrorConstant.INTERNAL_ERROR).build();
    }
}

