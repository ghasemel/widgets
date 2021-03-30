package com.elyasi.assignments.widgets.exception.defined.badRequest;

/**
 * Created by Ghasem on 27/03/2021
 */
public class InvalidRequestBodyException extends BadRequestException {

    public InvalidRequestBodyException(Object value) {
        super(String.format("Invalid request body: %s", value));
    }
}
