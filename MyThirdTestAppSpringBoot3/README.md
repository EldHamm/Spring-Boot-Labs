# MySecondTestAppSpringBoot

Лабораторная работа №3 по Spring Boot: доработка REST-сервиса из лабораторной работы №2. Добавлены сервисы, которые изменяют исходящий `response`, перечисления вместо строк и подробное логирование через Lombok `@Slf4j`.

## Что изменилось по сравнению с ЛР2

- Пакет `util`: класс `DateTimeUtil` с методом `getCustomFormat()`, который возвращает `SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")` (время в UTC).
- Пакет `service`: интерфейс `ModifyResponseService` и две реализации с `@Qualifier`:
  - `ModifyOperationUidResponseService` заменяет `operationUid` на случайный UUID;
  - `ModifySystemTimeResponseService` обновляет `systemTime` через `DateTimeUtil`. Именно она подключена в `MyController`.
- Пакет `model`: перечисления `Codes`, `ErrorCodes`, `ErrorMessages` и `Systems`. Поля `code`, `errorCode`, `errorMessage` в `Response` теперь имеют типы этих перечислений, а поле `systemName` в `Request` имеет тип `Systems`.
- `Request` получил собственный `toString()` для понятного вывода в консоль.
- `MyController` логирует входящий `request`, каждое изменение `response` и исходящий `response`; ошибки пишутся через `log.error()` вместе с сообщениями из `bindingResult`.

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
    │   │   │   ├── ModifyResponseService.java
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

**В IntelliJ IDEA:** `File → Open` → выбрать папку проекта → дождаться загрузки Maven-зависимостей → открыть класс `MySecondTestAppSpringBootApplication` и нажать Run.

**Из терминала** (нужны JDK 17+ и Maven):

```bash
mvn spring-boot:run
```

Сервис стартует на порту **8082**.

> Если IntelliJ IDEA не видит поле `log`, включите обработку аннотаций:
> `Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing`.

## API

`POST http://localhost:8082/feedback`, заголовок `Content-Type: application/json`.

### Запрос (Request)

| Ключ | Тип | Обязательность |
|------|-----|----------------|
| `uid` | String | Да, до 32 символов |
| `operationUid` | String | Да, до 32 символов |
| `systemName` | `ERP`, `CRM` или `WMS` | Нет |
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

Системы: `ERP` (Enterprise Resource Planning), `CRM` (Customer Relationship Management), `WMS` (Warehouse Management System). Если в `systemName` пришло другое значение, Spring отклоняет запрос ещё до контроллера со стандартным ответом 400.

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

| Ситуация | HTTP | `code` | `errorCode` | `errorMessage` |
|----------|------|--------|-------------|----------------|
| Всё в порядке | 200 | `success` | пусто | пусто |
| Не прошла валидация | 400 | `failed` | `ValidationException` | Ошибка валидации |
| `uid` равен `123` | 400 | `failed` | `UnsupportedCodeException` | Не поддерживаемая ошибка |
| Любая другая ошибка | 500 | `failed` | `UnknownException` | Произошла непредвиденная ошибка |

## Тестирование

В Postman нажмите `Import` и выберите файл `postman/MySecondTestAppSpringBoot.postman_collection.json`. В коллекции двенадцать запросов: корректный, по одному на каждое правило валидации, запрос со всеми ошибками сразу, `uid = 123` и три запроса с полем `systemName`.

## Логирование

В `application.properties` заданы уровни: `logging.level.org.springframework=ERROR` (Spring пишет только ошибки) и `logging.level.com.hammatov=DEBUG` (сообщения приложения видны полностью). Из-за первой настройки обычные строки Spring вроде `Tomcat started on port 8082` в консоли не появляются; о запуске говорят строка `Started MySecondTestAppSpringBootApplication` и сообщения приложения.

Пример для успешного запроса:

```text
Входящий request: Request{uid='1', operationUid='op-1', systemName=ERP, ...}
Создан начальный response: Response(uid=1, operationUid=op-1, ..., code=success, errorCode=, errorMessage=)
RequestValidationService: ошибок валидации нет
Валидация request пройдена
Проверка uid на значение 123 пройдена
ModifySystemTimeResponseService: systemTime обновлён: '...' -> '...'
Response изменён сервисом ModifySystemTimeResponseService: Response(...)
Исходящий response: Response(...)
```

Пример для запроса с пустым `uid` (уровень `ERROR`):

```text
RequestValidationService: request не прошёл валидацию, ошибок: 1, будет выброшено ValidationFailedException
Ошибка валидации request, найдено ошибок: 1
Поле 'uid': Поле uid обязательно (получено значение: '')
```

## Автор

Hammatov
