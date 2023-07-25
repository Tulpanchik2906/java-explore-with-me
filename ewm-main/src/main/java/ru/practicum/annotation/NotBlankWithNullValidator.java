package ru.practicum.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankWithNullValidator implements ConstraintValidator<NotBlankWithNull, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        } else {
            return !s.isBlank();
        }
    }
}
