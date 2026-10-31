package com.hammatov.MySecondTestAppSpringBoot.controller;

import com.hammatov.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import com.hammatov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import com.hammatov.MySecondTestAppSpringBoot.model.Codes;
import com.hammatov.MySecondTestAppSpringBoot.model.ErrorCodes;
import com.hammatov.MySecondTestAppSpringBoot.model.ErrorMessages;
import com.hammatov.MySecondTestAppSpringBoot.model.Positions;
import com.hammatov.MySecondTestAppSpringBoot.model.Request;
import com.hammatov.MySecondTestAppSpringBoot.model.Response;
import com.hammatov.MySecondTestAppSpringBoot.service.AnnualBonusService;
import com.hammatov.MySecondTestAppSpringBoot.service.ModifyRequestService;
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

import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifyRequestService;
    private final AnnualBonusService annualBonusService;

    // Реализаций ModifyResponseService две, поэтому нужную выбираем через @Qualifier
    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                        ModifyRequestService modifyRequestService,
                        AnnualBonusService annualBonusService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifyRequestService = modifyRequestService;
        this.annualBonusService = annualBonusService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {
        log.info("Входящий request: {}", request);
        Response response = createResponse(request);

        try {
            validationService.isValid(bindingResult);
            log.info("Валидация request пройдена");

            checkUid(request);
            calculateBonuses(request, response);

            // Меняет systemName на «Service 1» и отправляет request на Сервис 2
            modifyRequestService.modify(request);
        } catch (ValidationFailedException e) {
            logValidationErrors(bindingResult);
            return failed(response, ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("Не поддерживаемое значение в request: {}", e.getMessage());
            return failed(response, ErrorCodes.UNSUPPORTED_EXCEPTION, ErrorMessages.UNSUPPORTED, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Последний параметр-исключение логгер выводит вместе со стеком вызовов
            log.error("Непредвиденная ошибка при обработке request: {}, ошибок в bindingResult: {}",
                    e.getMessage(), bindingResult.getErrorCount(), e);
            return failed(response, ErrorCodes.UNKNOWN_EXCEPTION, ErrorMessages.UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Успешный путь: сервис обновляет systemTime прямо перед отправкой ответа
        response = modifyResponseService.modify(response);
        log.info("Исходящий response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Ответ сразу создаётся «успешным», при ошибках его поля меняет метод failed()
    private Response createResponse(Request request) {
        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("Создан начальный response: {}", response);
        return response;
    }

    private void checkUid(Request request) throws UnsupportedCodeException {
        if ("123".equals(request.getUid())) {
            throw new UnsupportedCodeException("Значение uid = 123 не поддерживается");
        }
        log.info("Проверка uid на значение 123 пройдена");
    }

    // Считает годовую премию, а для руководителей ещё и квартальную, и записывает их в response
    private void calculateBonuses(Request request, Response response) {
        Positions position = request.getPosition();
        Double salary = request.getSalary();
        Double bonus = request.getBonus();
        Integer workDays = request.getWorkDays();

        if (position == null || salary == null || bonus == null || workDays == null) {
            log.info("Премия не рассчитана: в request не заполнены position, salary, bonus или workDays");
            return;
        }

        response.setAnnualBonus(annualBonusService.calculate(position, salary, bonus, workDays));
        log.info("Годовая премия рассчитана: {}", response.getAnnualBonus());

        if (position.isManager()) {
            response.setQuarterlyBonus(annualBonusService.calculateQuarterlyBonus(position, salary, bonus, workDays));
            log.info("Квартальная премия рассчитана: {}", response.getQuarterlyBonus());
        } else {
            log.info("Квартальная премия не рассчитывается: должность {} не относится к руководителям", position);
        }
    }

    private void logValidationErrors(BindingResult bindingResult) {
        log.error("Ошибка валидации request, найдено ошибок: {}", bindingResult.getErrorCount());
        bindingResult.getFieldErrors().forEach(fieldError ->
                log.error("Поле '{}': {} (получено значение: '{}')",
                        fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue()));
    }

    // Помечает response как неуспешный и формирует HTTP-ответ с нужным статусом
    private ResponseEntity<Response> failed(Response response, ErrorCodes errorCode,
                                            ErrorMessages errorMessage, HttpStatus status) {
        response.setCode(Codes.FAILED);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        log.info("Response изменён после ошибки {}: {}", errorCode, response);
        log.info("Исходящий response: {}", response);
        return new ResponseEntity<>(response, status);
    }

}
