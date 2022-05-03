package com.devwue.member.validator;

import com.devwue.member.annotation.Enum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<Enum, String> {
    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String validValue, ConstraintValidatorContext context) {
        boolean result = false;
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (enumValue.toString().equals(validValue)
                        || (this.annotation.ignoreCase() && enumValue.toString().equalsIgnoreCase(validValue))) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
