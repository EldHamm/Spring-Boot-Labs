package com.hammatov.MySecondTestAppSpringBoot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {

    private String uid;
    private String operationUid;
    private String systemTime;

    // «success» или «failed»
    private String code;

    // «UnsupportedCodeException», «ValidationException», «UnknownException» или пустая строка
    private String errorCode;

    // Текст ошибки или пустая строка
    private String errorMessage;

}
