package com.demo.library.utils.customValidator.pattern.gender;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = GenderValidator.class)
public @interface GenderValidation {

    String message() default "Gender should be MALE or FEMALE.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
