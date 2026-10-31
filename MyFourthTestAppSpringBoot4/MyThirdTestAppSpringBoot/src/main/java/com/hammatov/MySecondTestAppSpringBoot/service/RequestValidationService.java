package com.hammatov.MySecondTestAppSpringBoot.service;

import com.hammatov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

@Slf4j
@Service
public class RequestValidationService implements ValidationService {

    @Override
    public void isValid(BindingResult bindingResult) throws ValidationFailedException {
        if (bindingResult.hasErrors()) {
            // Собираем все ошибки, а не только первую: «поле: сообщение; поле: сообщение»
            String details = bindingResult.getAllErrors().stream()
                    .map(error -> error instanceof FieldError fieldError
                            ? fieldError.getField() + ": " + fieldError.getDefaultMessage()
                            : error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            log.error("RequestValidationService: request не прошёл валидацию, ошибок: {}, будет выброшено ValidationFailedException",
                    bindingResult.getErrorCount());
            throw new ValidationFailedException(details);
        }
        log.info("RequestValidationService: ошибок валидации нет");
    }

}
