package com.demo.library.utils.customValidator.pattern.gender;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenderValidator implements ConstraintValidator<GenderValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile("^(MALE|FEMALE)$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
