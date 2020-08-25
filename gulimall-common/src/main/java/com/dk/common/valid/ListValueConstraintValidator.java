package com.dk.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {
    private Set<Integer> valueSet = new HashSet<>();

    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for (int val : vals) {
            this.valueSet.add(val);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return this.valueSet.contains(value);
    }
}
