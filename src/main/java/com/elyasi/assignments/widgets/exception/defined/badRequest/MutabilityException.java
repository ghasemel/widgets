package com.elyasi.assignments.widgets.exception.defined.badRequest;

/**
 * Created by Ghasem on 27/03/2021
 */
public class MutabilityException extends BadRequestException {

    public MutabilityException(String property) {
        super(String.format("%s attribute is readonly", property));
    }
}
