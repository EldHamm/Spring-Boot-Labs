# MyThirdTestAppSpringBoot (Сервис 2, получатель)

Лабораторная работа №4 по Spring Boot: взаимодействие двух сервисов через `RestTemplate`. Это **Сервис 2**: он принимает request от **Сервиса 1** (`MySecondTestAppSpringBoot`) или напрямую из Postman и считает, сколько миллисекунд прошло с момента отправки request до его получения.

```text
Postman --request--> Сервис 1 (порт 8082) --request--> Сервис 2 (порт 8084)
Postman <--response-- Сервис 1
```

## Что изменилось по сравнению с ЛР3

- Это копия сервиса из ЛР3: главный класс переименован в `MyThirdTestAppSpringBootApplication`, порт изменён на `8084`.
- В `MyController` добавлен расчёт разницы во времени: в самом начале метода фиксируется момент получения request, затем из него вычитается `systemTime` из request, и результат в миллисекундах пишется в лог уровня `INFO`.
- В `DateTimeUtil` добавлен метод `millisSince()`, который разбирает `systemTime` в формате ISO-8601 и возвращает разницу.
- В перечисление `Systems` добавлено значение `SERVICE_1` (в JSON `"Service 1"`), которое подставляет Сервис 1.

## Как считается разница во времени

Разница равна «момент получения request Сервисом 2 минус `systemTime` из request». Если отправлять запрос из Postman с `"systemTime": "{{$isoTimestamp}}"`, то в `systemTime` попадает момент нажатия Send, а Сервис 1 пересылает это поле без изменений. Поэтому в логе Сервиса 2 видно полное время от Send в Postman до получения request Сервисом 2, включая обработку в Сервисе 1.

Особые случаи:

- `systemTime` не в формате ISO-8601: в лог пишется предупреждение, запрос обрабатывается как обычно.
- `systemTime` пустой: предупреждение в лог, а валидация вернёт ошибку 400.
- `systemTime` в будущем: разница получится отрицательной, и в лог добавится предупреждение.
- Обе программы должны работать на одном компьютере или на компьютерах с синхронизированными часами, иначе результат будет искажён.

## Технологии

- Java 17
- Spring Boot 3.5 (Spring Web, Validation)
- Maven
- Lombok

## Структура проекта

```text
MyThirdTestAppSpringBoot
├── pom.xml
├── README.md
├── .gitignore
├── postman
│   └── MyThirdTestAppSpringBoot.postman_collection.json
└── src
    ├── main
    │   ├── java/com/hammatov/MySecondTestAppSpringBoot
    │   │   ├── MyThirdTestAppSpringBootApplication.java
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
        └── MyThirdTestAppSpringBootApplicationTests.java
```

Корневой пакет остался `com.hammatov.MySecondTestAppSpringBoot`, как в методичке: переименован только главный класс.

## Запуск

Нужны JDK 17+ и Maven. Сервис 2 лучше запускать первым.

- в IntelliJ IDEA: `File → Open` (лучше в новом окне) → папка проекта → класс `MyThirdTestAppSpringBootApplication` → Run;
- из терминала: `mvn spring-boot:run`.

Сервис 2 стартует на порту **8084**. Сервис 1 работает на 8082, одновременно два сервиса на одном порту запустить нельзя.

> Если IntelliJ IDEA не видит поле `log`, включите обработку аннотаций:
> `Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing`.

## API

`POST http://localhost:8084/feedback`, заголовок `Content-Type: application/json`. Формат request и response, правила валидации и коды ошибок такие же, как в ЛР3. Допустимые значения `systemName`: `ERP`, `CRM`, `WMS` и `Service 1`.

| Ситуация | HTTP | `code` | `errorCode` | `errorMessage` |
|----------|------|--------|-------------|----------------|
| Всё в порядке | 200 | `success` | пусто | пусто |
| Не прошла валидация | 400 | `failed` | `ValidationException` | Ошибка валидации |
| `uid` равен `123` | 400 | `failed` | `UnsupportedCodeException` | Не поддерживаемая ошибка |
| Любая другая ошибка | 500 | `failed` | `UnknownException` | Произошла непредвиденная ошибка |

## Тестирование

В Postman нажмите `Import` и выберите `postman/MyThirdTestAppSpringBoot.postman_collection.json`. В коллекции пять запросов для прямой отправки на Сервис 2: корректный, со старым `systemTime` (огромная разница), с `systemTime` не в формате ISO-8601, с пустым `uid` и с `uid = 123`. Основной сценарий (Postman, Сервис 1, Сервис 2) проверяется запросами из коллекции Сервиса 1.

## Логирование

Пример для запроса, пришедшего от Сервиса 1:

```text
Входящий request: Request{uid='1', ..., systemName=Service 1, systemTime='2026-10-20T16:30:00.123Z', ...}
Разница во времени между отправкой request (systemTime = 2026-10-20T16:30:00.123Z) и его получением Сервисом 2: 42 мс
Создан начальный response: Response(uid=1, ..., code=success, errorCode=, errorMessage=)
RequestValidationService: ошибок валидации нет
Валидация request пройдена
Проверка uid на значение 123 пройдена
ModifySystemTimeResponseService: systemTime обновлён: '...' -> '...'
Response изменён сервисом ModifySystemTimeResponseService: Response(...)
Исходящий response: Response(...)
```

## Загрузка на GitHub

Создайте на github.com **пустой** репозиторий `MyThirdTestAppSpringBoot` (без README и `.gitignore`), затем в cmd или PowerShell:

```text
cd C:\Users\ВАШ_ПОЛЬЗОВАТЕЛЬ\IdeaProjects\MyThirdTestAppSpringBoot
git init
git branch -M main
git add .
git status
git commit -m "Lab 4: Service 2 receives request and logs time difference"
git remote add origin https://github.com/ВАШ_ЛОГИН/MyThirdTestAppSpringBoot.git
git push -u origin main
```

## Автор

Hammatov
