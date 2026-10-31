# MySecondTestAppSpringBoot (Сервис 1)

Лабораторная работа №5 по Spring Boot: бизнес-логика расчёта премий и unit-тесты JUnit 5 с AssertJ. Это **Сервис 1** из ЛР4: он принимает request, считает годовую и квартальную премии, проверяет данные и пересылает request на Сервис 2 (`MyThirdTestAppSpringBoot`).

```text
Postman --request--> Сервис 1 (порт 8082) --request--> Сервис 2 (порт 8084)
Postman <--response-- Сервис 1
```

## Что изменилось по сравнению с ЛР4

- `Request` получил поля `position` (`Positions`), `salary` (`Double`), `bonus` (`Double`) и `workDays` (`Integer`); у каждого поля есть комментарий с описанием.
- Перечисление `Positions`: DEV (2.2), HR (1.2), TL (2.6), PO (2.8), TPM (2.5), CTO (3.0). Метод `getPositionCoefficient()` возвращает коэффициент, метод `isManager()` — признак руководителя (true для TL, PO, TPM, CTO).
- `AnnualBonusService` и `AnnualBonusServiceImpl`:
  - `calculate(pos, salary, bonus, workDays)` считает годовую премию по формуле `salary * bonus * [дней в году] * коэффициент / workDays`; в високосном году дней 366, в остальных 365;
  - `calculateQuarterlyBonus(...)` считает квартальную премию (четверть годовой) и работает только для руководителей, для остальных выбрасывает `IllegalArgumentException`.
- В `Response` добавлены `annualBonus` и `quarterlyBonus`. Если премия не рассчитывалась, поля в JSON отсутствуют.
- `MyController` разбит на небольшие методы (`createResponse`, `checkUid`, `calculateBonuses`, `logValidationErrors`, `failed`), метод `feedback()` стал заметно короче.
- Unit-тесты в `src/test/java`: `AnnualBonusServiceImplTest` (обычный и високосный год, все должности, квартальная премия, проверка признака руководителя) и `PositionsTest`.
- При запуске через Run в консоль выводятся текущий год с числом дней, а также коэффициенты всех должностей.
- Добавлена валидация необязательных полей: если `salary`, `bonus` или `workDays` указаны, они должны быть больше 0.

## Как определяется високосный год

Год берётся из часов системы (`Clock`) в момент расчёта, дней в году `Year.now(clock).length()`: 366 или 365. Часы передаются в конструктор `AnnualBonusServiceImpl(Clock)`, поэтому в тестах можно «подставить» любой год (2023 для обычного и 2024 для високосного).

## Технологии

- Java 17
- Spring Boot 3.5 (Spring Web, Validation)
- Maven
- Lombok
- JUnit 5 и AssertJ (входят в `spring-boot-starter-test`)

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
    │   │   │   ├── Positions.java
    │   │   │   ├── Request.java
    │   │   │   ├── Response.java
    │   │   │   └── Systems.java
    │   │   ├── service
    │   │   │   ├── AnnualBonusService.java
    │   │   │   ├── AnnualBonusServiceImpl.java
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
        ├── MySecondTestAppSpringBootApplicationTests.java
        ├── model
        │   └── PositionsTest.java
        └── service
            └── AnnualBonusServiceImplTest.java
```

## Запуск

Нужны JDK 17+ и Maven.

- в IntelliJ IDEA: `File → Open` → папка проекта → класс `MySecondTestAppSpringBootApplication` → Run;
- из терминала: `mvn spring-boot:run`.

Сервис стартует на порту **8082**. Request пересылается на Сервис 2 из ЛР4 (порт 8084): для успешного ответа его нужно запустить. Если Сервис 2 не нужен, поставьте в `application.properties` `service2.forwarding.enabled=false`. Сервис 2 из ЛР4 не знает новых полей (`position`, `salary` и других) и просто их игнорирует.

> Если IntelliJ IDEA не видит поле `log`, включите обработку аннотаций:
> `Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing`.

## Запуск тестов

- в IntelliJ IDEA: правый клик по папке `src/test/java` → `Run 'All Tests'`;
- из терминала: `mvn test`.

В `AnnualBonusServiceImplTest` и `PositionsTest` вместе 19 проверок (с учётом параметризованных), все должны проходить.

## API

`POST http://localhost:8082/feedback`, заголовок `Content-Type: application/json`.

```json
{
  "uid": "1",
  "operationUid": "op-1",
  "systemName": "ERP",
  "systemTime": "{{$isoTimestamp}}",
  "source": "web",
  "position": "TL",
  "salary": 100000.0,
  "bonus": 2.0,
  "workDays": 243,
  "communicationId": 1,
  "templateId": 1,
  "productCode": 1,
  "smsCode": 1
}
```

`{{$isoTimestamp}}` — динамическая переменная Postman: при отправке она заменяется текущим временем. Если отправляете запрос не из Postman, подставьте время вручную.

| Ключ | Тип | Обязательность |
|------|-----|----------------|
| `uid` | String | Да, до 32 символов |
| `operationUid` | String | Да, до 32 символов |
| `systemName` | `ERP`, `CRM`, `WMS`, `Service 1` | Нет |
| `systemTime` | String | Да |
| `source` | String | Нет |
| `position` | `DEV`, `HR`, `TL`, `PO`, `TPM`, `CTO` | Нет |
| `salary` | Double | Нет, если указан, то больше 0 |
| `bonus` | Double | Нет, если указан, то больше 0 |
| `workDays` | Integer | Нет, если указан, то больше 0 |
| `communicationId` | int | От 1 до 100000 |
| `templateId` | int | Нет |
| `productCode` | int | Нет |
| `smsCode` | int | Нет |

Премия считается, только если заполнены все четыре поля `position`, `salary`, `bonus` и `workDays`. Пример ответа для руководителя (TL) в невисокосном году:

```json
{
  "uid": "1",
  "operationUid": "op-1",
  "systemTime": "2026-10-20T16:30:00.123Z",
  "code": "success",
  "annualBonus": 781069.9588477366,
  "quarterlyBonus": 195267.48971193415,
  "errorCode": "",
  "errorMessage": ""
}
```

Для DEV и HR в ответе будет только `annualBonus`. Коды ошибок и HTTP-статусы такие же, как в ЛР4.

## Тестирование в Postman

В Postman нажмите `Import` и выберите `postman/MySecondTestAppSpringBoot.postman_collection.json`. В коллекции 20 запросов: корректный, по одному на каждое правило валидации, `uid = 123`, все шесть должностей, запрос без данных для премии, запрос с `workDays = 0`, отрицательный оклад и несуществующая должность.

## Логирование

Пример для запроса с должностью TL:

```text
Входящий request: Request{uid='1', ..., position=TL, salary=100000.0, bonus=2.0, workDays=243, ...}
Создан начальный response: Response(...)
Валидация request пройдена
Проверка uid на значение 123 пройдена
AnnualBonusServiceImpl: годовая премия: должность TL, оклад 100000.0, бонус 2.0, рабочих дней 243, дней в году 365 -> 781069.9588477366
Годовая премия рассчитана: 781069.9588477366
AnnualBonusServiceImpl: годовая премия: ... (расчёт внутри квартальной премии)
AnnualBonusServiceImpl: квартальная премия = годовая премия / 4 = 195267.48971193415
Квартальная премия рассчитана: 195267.48971193415
ModifySystemNameRequestService: ...
Исходящий response: Response(...)
```

## Загрузка на GitHub

Если папка уже привязана к GitHub (репозиторий с предыдущих лабораторных):

```text
cd C:\Users\ВАШ_ПОЛЬЗОВАТЕЛЬ\IdeaProjects\MySecondTestAppSpringBoot
git pull origin main
git add .
git status
git commit -m "Lab 5: bonus calculation and JUnit 5 tests"
git push origin main
```

Если репозитория ещё нет, создайте на github.com **пустой** репозиторий и выполните:

```text
git init
git branch -M main
git add .
git commit -m "Lab 5: bonus calculation and JUnit 5 tests"
git remote add origin https://github.com/ВАШ_ЛОГИН/MySecondTestAppSpringBoot.git
git push -u origin main
```

## Автор

Hammatov
