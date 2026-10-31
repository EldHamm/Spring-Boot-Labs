package com.hammatov.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    // Уникальный идентификатор сообщения: обязателен, до 32 символов
    @NotBlank(message = "Поле uid обязательно")
    @Size(max = 32, message = "Поле uid должно содержать не более 32 символов")
    private String uid;

    // Уникальный идентификатор операции: обязателен, до 32 символов
    @NotBlank(message = "Поле operationUid обязательно")
    @Size(max = 32, message = "Поле operationUid должно содержать не более 32 символов")
    private String operationUid;

    // Имя системы отправителя: необязательное поле
    private String systemName;

    // Время создания сообщения: обязательно
    @NotBlank(message = "Поле systemTime обязательно")
    private String systemTime;

    // Наименование ресурса: необязательное поле
    private String source;

    // Уникальный идентификатор коммуникации: от 1 до 100000
    @Min(value = 1, message = "Поле communicationId должно быть не меньше 1")
    @Max(value = 100000, message = "Поле communicationId должно быть не больше 100000")
    private int communicationId;

    // Остальные поля необязательные
    private int templateId;
    private int productCode;
    private int smsCode;

}
