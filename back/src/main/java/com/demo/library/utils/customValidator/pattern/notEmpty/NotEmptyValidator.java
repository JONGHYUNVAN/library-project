package com.demo.library.utils.customValidator.pattern.notEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotEmptyValidator implements ConstraintValidator<NotEmptyValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile(".{2,}");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
