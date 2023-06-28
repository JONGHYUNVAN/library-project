package com.demo.library.utils.customValidator.pattern.notEmpty;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = NotEmptyValidator.class)
public @interface NotEmptyValidation {
    String fieldName();

    String message() default "{fieldName} should not be null.";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}


