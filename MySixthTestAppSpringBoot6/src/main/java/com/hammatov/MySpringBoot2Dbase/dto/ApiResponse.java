package com.hammatov.MySpringBoot2Dbase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

// Единый формат ответа всех CRUD-методов: признак успеха, сообщение и (если есть) данные
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    /** true, если операция выполнена успешно */
    private final boolean success;

    /** Понятное описание результата или причины неуспеха */
    private final String message;

    /** Данные операции (студент, дисциплина или список); у неуспешных ответов и удаления их нет */
    private final Object data;

    private ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse ok(String message, Object data) {
        return new ApiResponse(true, message, data);
    }

    public static ApiResponse failure(String message) {
        return new ApiResponse(false, message, null);
    }

}
