package com.hammatov.MySecondTestAppSpringBoot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
