package com.elyasi.assignments.widgets.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Ghasem on 27/03/2021
 */
@Slf4j
public class AreaValidator implements ConstraintValidator<AreaTag, String> {
    private static final String FORMAT = "-?[0-9]+:-?[0-9]+,-?[0-9]+:-?[0-9]+";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null)
            return true;

        log.debug("validate area {}", value);

        return value.matches(FORMAT);
    }

    @Override
    public void initialize(AreaTag constraintAnnotation) {
        // nothing!
    }
}
