package com.miro.assignments.widgets.exception.defined.bad;

/**
 * Created by taaelgh1 on 27/03/2021
 */
public class MutabilityException extends BadRequestException {

    public MutabilityException(String property) {
        super(String.format("%s attribute is readonly", property));
    }
}
