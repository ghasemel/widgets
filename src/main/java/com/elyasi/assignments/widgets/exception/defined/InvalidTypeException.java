package com.elyasi.assignments.widgets.exception.defined;

/**
 * Created by Ghasem on 28/03/2021
 */
public class InvalidTypeException extends RuntimeException {

    public InvalidTypeException(String type) {
        super(String.format("Type %s is not defined", type));
    }
}
