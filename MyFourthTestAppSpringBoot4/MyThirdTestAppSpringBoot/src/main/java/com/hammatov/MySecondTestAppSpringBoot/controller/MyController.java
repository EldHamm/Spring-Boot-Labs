package com.hammatov.MySecondTestAppSpringBoot.controller;

import com.hammatov.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import com.hammatov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import com.hammatov.MySecondTestAppSpringBoot.model.Codes;
import com.hammatov.MySecondTestAppSpringBoot.model.ErrorCodes;
import com.hammatov.MySecondTestAppSpringBoot.model.ErrorMessages;
import com.hammatov.MySecondTestAppSpringBoot.model.Request;
import com.hammatov.MySecondTestAppSpringBoot.model.Response;
import com.hammatov.MySecondTestAppSpringBoot.service.ModifyResponseService;
import com.hammatov.MySecondTestAppSpringBoot.service.ValidationService;
import com.hammatov.MySecondTestAppSpringBoot.util.DateTimeUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;

    // Реализаций ModifyResponseService две, поэтому нужную выбираем через @Qualifier
    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {

        // Момент получения запроса фиксируем самым первым действием, чтобы замер был точным
        Instant receivedAt = Instant.now();

        log.info("Входящий request: {}", request);
        logTimeDifference(request.getSystemTime(), receivedAt);

        // Ответ сразу создаётся «успешным», при ошибках его поля меняются в блоках catch
        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("Создан начальный response: {}", response);

        try {
            validationService.isValid(bindingResult);
            log.info("Валидация request пройдена");

            if ("123".equals(request.getUid())) {
                throw new UnsupportedCodeException("Значение uid = 123 не поддерживается");
            }
            log.info("Проверка uid на значение 123 пройдена");
        } catch (ValidationFailedException e) {
            log.error("Ошибка валидации request, найдено ошибок: {}", bindingResult.getErrorCount());
            bindingResult.getFieldErrors().forEach(fieldError ->
                    log.error("Поле '{}': {} (получено значение: '{}')",
                            fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue()));

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("Response изменён после ValidationFailedException: {}", response);
            log.info("Исходящий response: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("Не поддерживаемое значение в request: {}", e.getMessage());

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.info("Response изменён после UnsupportedCodeException: {}", response);
            log.info("Исходящий response: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Последний параметр-исключение логгер выводит вместе со стеком вызовов
            log.error("Непредвиденная ошибка при обработке request: {}, ошибок в bindingResult: {}",
                    e.getMessage(), bindingResult.getErrorCount(), e);

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("Response изменён после непредвиденной ошибки: {}", response);
            log.info("Исходящий response: {}", response);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Успешный путь: сервис обновляет systemTime прямо перед отправкой ответа
        response = modifyResponseService.modify(response);
        log.info("Response изменён сервисом ModifySystemTimeResponseService: {}", response);

        log.info("Исходящий response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Считает, сколько миллисекунд прошло с момента отправки request (поле systemTime)
    // до момента его получения этим сервисом, и пишет результат в лог
    private void logTimeDifference(String systemTime, Instant receivedAt) {
        if (systemTime == null || systemTime.isBlank()) {
            log.warn("Разница во времени не рассчитана: в request нет systemTime");
            return;
        }
        try {
            long diffMillis = DateTimeUtil.millisSince(systemTime, receivedAt);
            log.info("Разница во времени между отправкой request (systemTime = {}) и его получением Сервисом 2: {} мс",
                    systemTime, diffMillis);
            if (diffMillis < 0) {
                log.warn("Разница отрицательная: systemTime указывает на момент позже получения запроса "
                        + "(время задано вручную или часы отправителя спешат)");
            }
        } catch (DateTimeParseException e) {
            log.warn("Разница во времени не рассчитана: systemTime '{}' не в формате ISO-8601, например 2023-10-07T07:47:56.503Z",
                    systemTime);
        }
    }

}
