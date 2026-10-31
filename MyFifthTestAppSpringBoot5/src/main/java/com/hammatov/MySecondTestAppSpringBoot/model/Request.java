package com.hammatov.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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

    /** Уникальный идентификатор сообщения: обязателен, до 32 символов */
    @NotBlank(message = "Поле uid обязательно")
    @Size(max = 32, message = "Поле uid должно содержать не более 32 символов")
    private String uid;

    /** Уникальный идентификатор операции: обязателен, до 32 символов */
    @NotBlank(message = "Поле operationUid обязательно")
    @Size(max = 32, message = "Поле operationUid должно содержать не более 32 символов")
    private String operationUid;

    /** Имя системы отправителя: необязательное поле, одно из значений ERP, CRM, WMS, Service 1 */
    private Systems systemName;

    /** Время создания сообщения: обязательно */
    @NotBlank(message = "Поле systemTime обязательно")
    private String systemTime;

    /** Наименование ресурса: необязательное поле */
    private String source;

    /** Должность сотрудника: необязательное поле, одно из значений DEV, HR, TL, PO, TPM, CTO */
    private Positions position;

    /** Оклад сотрудника: необязательное поле, если указан, то больше 0 */
    @Positive(message = "Поле salary должно быть больше 0")
    private Double salary;

    /** Коэффициент премии: необязательное поле, если указан, то больше 0 */
    @Positive(message = "Поле bonus должно быть больше 0")
    private Double bonus;

    /** Количество рабочих дней в году: необязательное поле, если указано, то больше 0 */
    @Positive(message = "Поле workDays должно быть больше 0")
    private Integer workDays;

    /** Уникальный идентификатор коммуникации: от 1 до 100000 */
    @Min(value = 1, message = "Поле communicationId должно быть не меньше 1")
    @Max(value = 100000, message = "Поле communicationId должно быть не больше 100000")
    private int communicationId;

    /** Уникальный идентификатор шаблона: необязательное поле */
    private int templateId;

    /** Код продукта: необязательное поле */
    private int productCode;

    /** Смс код: необязательное поле */
    private int smsCode;

    // Одной строкой выводит все поля запроса, чтобы запрос было удобно читать в консоли
    @Override
    public String toString() {
        return "Request{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName=" + systemName +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", position=" + position +
                ", salary=" + salary +
                ", bonus=" + bonus +
                ", workDays=" + workDays +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                '}';
    }

}
