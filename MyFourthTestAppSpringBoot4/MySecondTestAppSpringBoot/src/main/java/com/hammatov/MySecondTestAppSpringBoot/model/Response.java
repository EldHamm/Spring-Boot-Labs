package com.hammatov.MySecondTestAppSpringBoot.model;

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

    private String uid;
    private String operationUid;
    private String systemTime;

    // В JSON: «success» или «failed»
    private Codes code;

    // В JSON: пустая строка, «ValidationException», «UnsupportedCodeException» или «UnknownException»
    private ErrorCodes errorCode;

    // В JSON: пустая строка или текст ошибки
    private ErrorMessages errorMessage;

}
