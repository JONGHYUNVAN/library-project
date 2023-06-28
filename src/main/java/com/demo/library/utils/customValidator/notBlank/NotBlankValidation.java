package com.demo.library.utils.customValidator.notBlank;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = NotBlankValidator.class)
public @interface NotBlankValidation {
    String fieldName() default "fieldName";

    String message() default "{fieldName} should not be null.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

