# MySecondTestAppSpringBoot (Сервис 1, отправитель)

Лабораторная работа №4 по Spring Boot: взаимодействие двух сервисов через `RestTemplate`. Это **Сервис 1**: он принимает request от Postman, проверяет его, подставляет в поле `systemName` значение `Service 1` и пересылает на **Сервис 2** (`MyThirdTestAppSpringBoot`).

```text
Postman --request--> Сервис 1 (порт 8082) --request--> Сервис 2 (порт 8084)
Postman <--response-- Сервис 1
```

## Что изменилось по сравнению с ЛР3

- Интерфейс `ModifyRequestService` и его реализация `ModifySystemNameRequestService`: ставит `systemName = "Service 1"` и отправляет request на `http://localhost:8084/feedback` через `RestTemplate.exchange()`.
- В `MyController` внедрён `ModifyRequestService`. Отправка происходит автоматически, после того как request прошёл валидацию и проверку `uid`.
- В перечисление `Systems` добавлено значение `SERVICE_1`, которое в JSON выглядит как `"Service 1"`.
- В `Response` добавлены `@NoArgsConstructor` и `@AllArgsConstructor`, чтобы Сервис 1 мог разобрать JSON-ответ Сервиса 2.
- Если Сервис 2 недоступен или вернул ошибку, Сервис 1 отвечает `code = failed`, `errorCode = UnknownException` (HTTP 500).

## Технологии

- Java 17
- Spring Boot 3.5 (Spring Web, Validation)
- Maven
- Lombok

## Структура проекта

```text
MySecondTestAppSpringBoot
├── pom.xml
├── README.md
├── .gitignore
├── postman
│   └── MySecondTestAppSpringBoot.postman_collection.json
└── src
    ├── main
    │   ├── java/com/hammatov/MySecondTestAppSpringBoot
    │   │   ├── MySecondTestAppSpringBootApplication.java
    │   │   ├── controller
    │   │   │   └── MyController.java
    │   │   ├── exception
    │   │   │   ├── UnsupportedCodeException.java
    │   │   │   └── ValidationFailedException.java
    │   │   ├── model
    │   │   │   ├── Codes.java
    │   │   │   ├── ErrorCodes.java
    │   │   │   ├── ErrorMessages.java
    │   │   │   ├── Request.java
    │   │   │   ├── Response.java
    │   │   │   └── Systems.java
    │   │   ├── service
    │   │   │   ├── ModifyOperationUidResponseService.java
    │   │   │   ├── ModifyRequestService.java
    │   │   │   ├── ModifyResponseService.java
    │   │   │   ├── ModifySystemNameRequestService.java
    │   │   │   ├── ModifySystemTimeResponseService.java
    │   │   │   ├── RequestValidationService.java
    │   │   │   └── ValidationService.java
    │   │   └── util
    │   │       └── DateTimeUtil.java
    │   └── resources
    │       └── application.properties
    └── test/java/com/hammatov/MySecondTestAppSpringBoot
        └── MySecondTestAppSpringBootApplicationTests.java
```

## Запуск

Нужны JDK 17+ и Maven. Сервисы запускаются независимо, в любом порядке, но для успешной пересылки Сервис 2 должен работать.

1. Запустите **Сервис 2** (`MyThirdTestAppSpringBoot`, порт 8084).
2. Запустите **Сервис 1**:
   - в IntelliJ IDEA: `File → Open` → папка проекта → класс `MySecondTestAppSpringBootApplication` → Run;
   - из терминала: `mvn spring-boot:run`.

Сервис 1 стартует на порту **8082**.

> Если IntelliJ IDEA не видит поле `log`, включите обработку аннотаций:
> `Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing`.

## API

`POST http://localhost:8082/feedback`, заголовок `Content-Type: application/json`.

```json
{
  "uid": "1",
  "operationUid": "op-1",
  "systemName": "ERP",
  "systemTime": "{{$isoTimestamp}}",
  "source": "web",
  "communicationId": 1,
  "templateId": 1,
  "productCode": 1,
  "smsCode": 1
}
```

`{{$isoTimestamp}}` — динамическая переменная Postman: при отправке она заменяется текущим временем в формате ISO-8601 (например, `2026-10-20T16:30:00.123Z`). Благодаря ей Сервис 2 может посчитать, сколько миллисекунд прошло с нажатия кнопки Send. Если отправляете запрос не из Postman, подставьте текущее время вручную.

Правила валидации, поля и ответы такие же, как в ЛР3:

| Ситуация | HTTP | `code` | `errorCode` | `errorMessage` |
|----------|------|--------|-------------|----------------|
| Всё в порядке, request переслан на Сервис 2 | 200 | `success` | пусто | пусто |
| Не прошла валидация | 400 | `failed` | `ValidationException` | Ошибка валидации |
| `uid` равен `123` | 400 | `failed` | `UnsupportedCodeException` | Не поддерживаемая ошибка |
| Любая другая ошибка, в том числе Сервис 2 недоступен | 500 | `failed` | `UnknownException` | Произошла непредвиденная ошибка |

В ответе Postman всегда приходит response Сервиса 1; response Сервиса 2 записывается только в лог.

## Тестирование

В Postman нажмите `Import` и выберите `postman/MySecondTestAppSpringBoot.postman_collection.json`. В коллекции 14 запросов: корректный, по одному на каждое правило валидации, `uid = 123`, три запроса с `systemName`, запрос при остановленном Сервисе 2 и запрос с `systemTime` не в формате ISO-8601.

## Логирование

В `application.properties` заданы `logging.level.org.springframework=ERROR` и `logging.level.com.hammatov=DEBUG`, поэтому обычные строки Spring (например, про Tomcat) в консоли не появляются. Пример для успешного запроса:

```text
Входящий request: Request{uid='1', ..., systemName=ERP, ...}
Создан начальный response: Response(uid=1, ..., code=success, errorCode=, errorMessage=)
RequestValidationService: ошибок валидации нет
Валидация request пройдена
Проверка uid на значение 123 пройдена
ModifySystemNameRequestService: systemName изменён: 'ERP' -> 'Service 1'
ModifySystemNameRequestService: отправка request на Сервис 2 (http://localhost:8084/feedback): Request{... systemName=Service 1 ...}
ModifySystemNameRequestService: ответ Сервиса 2: HTTP 200 OK, body: Response(...)
Request изменён и переслан на Сервис 2: Request{... systemName=Service 1 ...}
ModifySystemTimeResponseService: systemTime обновлён: '...' -> '...'
Исходящий response: Response(...)
```

## Загрузка на GitHub

Создайте на github.com **пустой** репозиторий `MySecondTestAppSpringBoot` (без README и `.gitignore`), затем в cmd или PowerShell:

```text
cd C:\Users\ВАШ_ПОЛЬЗОВАТЕЛЬ\IdeaProjects\MySecondTestAppSpringBoot
git init
git branch -M main
git add .
git status
git commit -m "Lab 4: Service 1 sends request to Service 2 via RestTemplate"
git remote add origin https://github.com/ВАШ_ЛОГИН/MySecondTestAppSpringBoot.git
git push -u origin main
```

Если репозиторий уже привязан к GitHub (например, с ЛР2 и ЛР3), пропустите `git init` и `git remote add`, а вместо них выполните `git pull origin main`.

## Автор

Hammatov
