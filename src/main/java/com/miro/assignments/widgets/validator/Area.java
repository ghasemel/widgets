package com.miro.assignments.widgets.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {AreaValidator.class}
)
public @interface Area {
    String message() default "Invalid area format - acceptable format: X1:Y1,X2:Y2";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
