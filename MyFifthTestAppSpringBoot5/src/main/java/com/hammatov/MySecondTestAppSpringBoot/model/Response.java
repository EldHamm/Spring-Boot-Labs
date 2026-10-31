package com.hammatov.MySecondTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @NoArgsConstructor нужен, чтобы Сервис 1 мог превратить JSON-ответ Сервиса 2 обратно в объект Response
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    /** Уникальный идентификатор сообщения */
    private String uid;

    /** Уникальный идентификатор операции */
    private String operationUid;

    /** Время создания ответа */
    private String systemTime;

    /** Результат обработки: «success» или «failed» */
    private Codes code;

    /** Годовая премия; поле отсутствует в JSON, если премия не рассчитывалась */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double annualBonus;

    /** Квартальная премия; поле есть в JSON только для руководителей */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double quarterlyBonus;

    /** Код ошибки: пустая строка, «ValidationException», «UnsupportedCodeException» или «UnknownException» */
    private ErrorCodes errorCode;

    /** Сообщение об ошибке: пустая строка или текст ошибки */
    private ErrorMessages errorMessage;

}
