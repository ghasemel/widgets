package com.elyasi.assignments.widgets.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {AreaValidator.class}
)
public @interface AreaTag {
    static final String MSG = "Invalid area format - acceptable format: X1:Y1,X2:Y2";

    String message() default MSG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
