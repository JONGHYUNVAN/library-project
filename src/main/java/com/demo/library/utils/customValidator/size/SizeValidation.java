package com.demo.library.utils.customValidator.size;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = SizeValidator.class)
public @interface SizeValidation {
    int max();
    String message() default "{fieldName} should be at most {max} characters.";
    String fieldName();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}



