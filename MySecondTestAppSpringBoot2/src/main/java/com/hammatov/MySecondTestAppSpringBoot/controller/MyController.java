package com.hammatov.MySecondTestAppSpringBoot.controller;

import com.hammatov.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import com.hammatov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import com.hammatov.MySecondTestAppSpringBoot.model.Request;
import com.hammatov.MySecondTestAppSpringBoot.model.Response;
import com.hammatov.MySecondTestAppSpringBoot.service.ValidationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
public class MyController {

    // Время ответа в формате 2023-10-07T07:47:56.503Z (всегда в UTC)
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);

    private final ValidationService validationService;

    @Autowired
    public MyController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {

        log.info("POST /feedback: получен запрос: {}", request);

        // Ответ сразу создаётся «успешным», при ошибках его поля меняются в блоках catch
        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(TIME_FORMAT.format(Instant.now()))
                .code("success")
                .errorCode("")
                .errorMessage("")
                .build();

        try {
            validationService.isValid(bindingResult);

            if ("123".equals(request.getUid())) {
                throw new UnsupportedCodeException("Значение uid = 123 не поддерживается");
            }
        } catch (ValidationFailedException e) {
            log.warn("POST /feedback: запрос отклонён, errorCode = ValidationException");
            response.setCode("failed");
            response.setErrorCode("ValidationException");
            response.setErrorMessage("Ошибка валидации");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.warn("POST /feedback: запрос отклонён: {}", e.getMessage());
            response.setCode("failed");
            response.setErrorCode("UnsupportedCodeException");
            response.setErrorMessage("Не поддерживаемая ошибка");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("POST /feedback: непредвиденная ошибка", e);
            response.setCode("failed");
            response.setErrorCode("UnknownException");
            response.setErrorMessage("Произошла непредвиденная ошибка");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("POST /feedback: запрос обработан успешно, ответ: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
