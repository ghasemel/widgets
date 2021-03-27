package com.elyasi.assignments.widgets.exception.defined.bad;

/**
 * Created by Ghasem on 27/03/2021
 */
public class InvalidValueException extends BadRequestException {

    public InvalidValueException(String property, Object value) {
        super(String.format("Invalid value %s for %s", value, property));
    }
}
