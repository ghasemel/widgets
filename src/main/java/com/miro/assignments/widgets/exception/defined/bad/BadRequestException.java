package com.miro.assignments.widgets.exception.defined.bad;

/**
 * Created by taaelgh1 on 27/03/2021
 */
public class BadRequestException extends RuntimeException {


    public BadRequestException(String message) {
        super(message);
    }
}