package com.demo.library.utils.customValidator.phoneNumberValidation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidation, String> {
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile("^010-\\d{4}-\\d{4}$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
