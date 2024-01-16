package com.demo.library.utils.customValidator.size;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SizeValidator implements ConstraintValidator<SizeValidation, String> {
    private int max;
    @Override
    public void initialize(SizeValidation constraintAnnotation) {
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value.length() <= max;
    }
}
