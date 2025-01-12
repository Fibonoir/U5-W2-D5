package com.epicode.it.tavel_manager.validation;

import com.epicode.it.tavel_manager.exception.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@Service
public class ValidationService {

    private final Validator validator;

    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    public void validate(Object target) {
        BindingResult bindingResult = new BeanPropertyBindingResult(target, target.getClass().getName());
        validator.validate(target, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
    }
}
