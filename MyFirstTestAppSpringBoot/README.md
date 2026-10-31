# MyFirstTestAppSpringBoot

Лабораторная работа №1 по Spring Boot: простейшее веб-приложение с GET-эндпоинтами, коллекциями `ArrayList` и `HashMap` и логированием через Lombok `@Slf4j`.

## Технологии

- Java 17
- Spring Boot 3.5 (Spring Web, встроенный Tomcat)
- Maven
- Lombok (`@Slf4j`)

## Структура проекта

```text
MyFirstTestAppSpringBoot
├── pom.xml
├── README.md
├── .gitignore
└── src
    ├── main
    │   ├── java/com/hammatov/MyFirstTestAppSpringBoot
    │   │   ├── MyFirstTestAppSpringBootApplication.java
    │   │   └── hello
    │   │       └── HelloController.java
    │   └── resources
    │       └── application.properties
    └── test/java/com/hammatov/MyFirstTestAppSpringBoot
        └── MyFirstTestAppSpringBootApplicationTests.java
```

## Запуск

**В IntelliJ IDEA:** `File → Open` → выбрать папку проекта → дождаться загрузки Maven-зависимостей → открыть класс `MyFirstTestAppSpringBootApplication` и нажать Run.

**Из терминала** (нужны JDK 17+ и Maven):

```bash
mvn spring-boot:run
```

Приложение стартует на <http://localhost:8080>. В консоли должны появиться строки `Tomcat started on port 8080 (http)` и `Started MyFirstTestAppSpringBootApplication`, а следом сообщения о запуске от `log.info()`.

> Если IntelliJ IDEA не видит поле `log`, включите обработку аннотаций:
> `Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing`.

## Эндпоинты

| Метод | URL | Что делает |
|-------|-----|-----------|
| GET | `/hello` | Возвращает `Hello World!` |
| GET | `/hello?name=Petya` | Возвращает `Hello Petya!` |
| GET | `/update-array?s=текст` | Добавляет строку в `ArrayList<String>` (список создаётся при первом вызове) |
| GET | `/show-array` | Показывает все элементы `ArrayList` |
| GET | `/update-map?s=текст` | Добавляет строку в `HashMap<Integer, String>` (ключ растёт автоматически: 1, 2, 3…) |
| GET | `/show-map` | Показывает все элементы `HashMap` |
| GET | `/show-all-lenght` | Показывает количество элементов в `ArrayList` и `HashMap` |

Написание `lenght` в последнем URL сохранено, как в методичке.

## Логирование

Каждый GET-метод пишет в консоль сообщения уровня `INFO`: какой параметр получен, что было сделано с коллекцией и что вернулось клиенту. Пример после запросов `/update-array?s=Spring` и `/show-array`:

```text
GET /update-array: получен параметр s = 'Spring'
GET /update-array: первый вызов, создан пустой ArrayList<String>
GET /update-array: строка 'Spring' добавлена, элементов в ArrayList: 1
GET /show-array: запрос на вывод элементов ArrayList
GET /show-array: в ArrayList 1 элемент(ов): [Spring]
```

## Автор

Hammatov
