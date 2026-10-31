package com.hammatov.MySecondTestAppSpringBoot.service;

import com.hammatov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import org.springframework.validation.BindingResult;

public interface ValidationService {

    void isValid(BindingResult bindingResult) throws ValidationFailedException;

}
