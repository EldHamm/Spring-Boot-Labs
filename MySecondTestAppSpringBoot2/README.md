# MySecondTestAppSpringBoot

Лабораторная работа №2 по Spring Boot: простой REST-сервис с валидацией входящего сообщения, собственными исключениями и логированием через Lombok `@Slf4j`.

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
    │   │   │   ├── Request.java
    │   │   │   └── Response.java
    │   │   └── service
    │   │       ├── RequestValidationService.java
    │   │       └── ValidationService.java
    │   └── resources
    │       └── application.properties
    └── test/java/com/hammatov/MySecondTestAppSpringBoot
        └── MySecondTestAppSpringBootApplicationTests.java
```

## Запуск

**В IntelliJ IDEA:** `File → Open` → выбрать папку проекта → дождаться загрузки Maven-зависимостей → открыть класс `MySecondTestAppSpringBootApplication` и нажать Run.

**Из терминала** (нужны JDK 17+ и Maven):

```bash
mvn spring-boot:run
```

Сервис стартует на порту **8082** (`server.port=8082` в `application.properties`).

> Если IntelliJ IDEA не видит поле `log`, включите обработку аннотаций:
> `Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing`.

## API

`POST http://localhost:8082/feedback`, заголовок `Content-Type: application/json`.

### Запрос (Request)

| Ключ | Тип | Обязательность |
|------|-----|----------------|
| `uid` | String | Да, до 32 символов |
| `operationUid` | String | Да, до 32 символов |
| `systemName` | String | Нет |
| `systemTime` | String | Да |
| `source` | String | Нет |
| `communicationId` | int | От 1 до 100000 |
| `templateId` | int | Нет |
| `productCode` | int | Нет |
| `smsCode` | int | Нет |

```json
{
  "uid": "1",
  "operationUid": "op-1",
  "systemName": "ERP",
  "systemTime": "2023-10-07T07:47:56.503Z",
  "source": "web",
  "communicationId": 1,
  "templateId": 1,
  "productCode": 1,
  "smsCode": 1
}
```

### Ответ (Response)

```json
{
  "uid": "1",
  "operationUid": "op-1",
  "systemTime": "2026-10-20T16:30:00.123Z",
  "code": "success",
  "errorCode": "",
  "errorMessage": ""
}
```

`systemTime` в ответе формирует сам сервис (UTC).

| Ситуация | HTTP | `code` | `errorCode` | `errorMessage` |
|----------|------|--------|-------------|----------------|
| Всё в порядке | 200 | `success` | пусто | пусто |
| Не прошла валидация | 400 | `failed` | `ValidationException` | Ошибка валидации |
| `uid` равен `123` | 400 | `failed` | `UnsupportedCodeException` | Не поддерживаемая ошибка |
| Любая другая ошибка | 500 | `failed` | `UnknownException` | Произошла непредвиденная ошибка |

Если тело запроса не является корректным JSON или в числовое поле пришла строка, Spring отклоняет запрос ещё до контроллера со стандартным ответом 400.

## Тестирование

Откройте Postman, нажмите `Import` и выберите файл `postman/MySecondTestAppSpringBoot.postman_collection.json`. В коллекции девять запросов: корректный, по одному на каждое правило валидации, запрос со всеми ошибками сразу и запрос с `uid = 123`.

## Логирование

Сервис пишет в консоль, что пришло, какая проверка сработала и чем закончилась обработка. Пример для запроса с пустым `uid`:

```text
POST /feedback: получен запрос: Request(uid=, operationUid=op-1, ...)
RequestValidationService: найдены ошибки валидации: uid: Поле uid обязательно
POST /feedback: запрос отклонён, errorCode = ValidationException
```

## Автор

Hammatov
