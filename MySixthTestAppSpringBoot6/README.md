# MySpringBoot2Dbase

Лабораторная работа №6 по Spring Boot: REST CRUD-сервис для работы со встроенной базой данных H2. Сервис управляет двумя таблицами: студентами (`STUDENTS`) и учебными дисциплинами (`SUBJECTS`). Доступ к данным реализован через `EntityManager` (слои controller, service, dao, entity), каждая операция пишет понятные сообщения в консоль через Lombok `@Slf4j`.

## Технологии

- Java 17
- Spring Boot 3.5 (Spring Web, Spring Data JPA)
- H2 Database (встроенный режим, данные хранятся в файле)
- Maven
- Lombok

## Структура проекта

```text
MySpringBoot2Dbase
├── pom.xml
├── README.md
├── .gitignore
├── postman
│   └── MySpringBoot2Dbase.postman_collection.json
└── src
    ├── main
    │   ├── java/com/hammatov/MySpringBoot2Dbase
    │   │   ├── MySpringBoot2DbaseApplication.java
    │   │   ├── controller
    │   │   │   └── MyController.java
    │   │   ├── dao
    │   │   │   ├── StudentDAO.java
    │   │   │   ├── StudentDAOImpl.java
    │   │   │   ├── SubjectDAO.java
    │   │   │   └── SubjectDAOImpl.java
    │   │   ├── dto
    │   │   │   └── ApiResponse.java
    │   │   ├── entity
    │   │   │   ├── Student.java
    │   │   │   └── Subject.java
    │   │   └── service
    │   │       ├── StudentService.java
    │   │       ├── StudentServiceImpl.java
    │   │       ├── SubjectService.java
    │   │       └── SubjectServiceImpl.java
    │   └── resources
    │       └── application.properties
    └── test/java/com/hammatov/MySpringBoot2Dbase
        └── MySpringBoot2DbaseApplicationTests.java
```

Папка `testdb` с файлом базы данных появится в корне проекта после первого запуска и не попадает в git.

## Запуск

Нужны JDK 17+ и Maven.

- в IntelliJ IDEA: `File → Open` → папка проекта → класс `MySpringBoot2DbaseApplication` → Run;
- из терминала: `mvn spring-boot:run`.

Сервис стартует на порту **8080**. Запускайте приложение из корня проекта: путь к базе `jdbc:h2:./testdb/students` считается от рабочего каталога. Таблицы `STUDENTS` и `SUBJECTS` Hibernate создаёт сам (`spring.jpa.hibernate.ddl-auto=update`), вручную ничего создавать не нужно.

> Если IntelliJ IDEA не видит поле `log`, включите обработку аннотаций:
> `Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing`.

## База данных H2

- Веб-консоль H2: <http://localhost:8080/h2-console>. Параметры входа: JDBC URL `jdbc:h2:./testdb/students`, пользователь `sa`, пароль пустой.
- Все SQL-запросы Hibernate выводятся в консоль приложения (`spring.jpa.show-sql=true`).
- Файл базы открыт приложением, поэтому DBeaver и другие внешние программы смогут подключиться к нему только после остановки приложения (встроенный режим H2 не допускает двух процессов). Пока приложение работает, пользуйтесь веб-консолью H2.
- Для таблиц заданы такие же ограничения, как в методичке: `name` до 15 символов, `surname` до 25, `faculty` до 20.

## REST API

Все пути начинаются с `/api`.

| Метод | URL | Операция |
|-------|-----|----------|
| GET | `/api/students` | Получение всех студентов |
| GET | `/api/students/{id}` | Получение одного студента |
| POST | `/api/students` | Добавление студента |
| PUT | `/api/students` | Изменение студента (в теле нужен `id`) |
| DELETE | `/api/students/{id}` | Удаление студента |
| GET | `/api/subjects` | Получение всех дисциплин |
| GET | `/api/subjects/{id}` | Получение одной дисциплины |
| POST | `/api/subjects` | Добавление дисциплины |
| PUT | `/api/subjects` | Изменение дисциплины (в теле нужен `id`) |
| DELETE | `/api/subjects/{id}` | Удаление дисциплины |

Пример тела POST-запроса для студента (при добавлении `id` не нужен, его назначает база данных; если передать, он игнорируется):

```json
{
  "name": "Иван",
  "surname": "Иванов",
  "faculty": "ИТ",
  "age": 20
}
```

Пример тела POST-запроса для дисциплины:

```json
{
  "name": "Математика",
  "description": "Высшая математика, первый курс"
}
```

### Формат ответа

Все методы возвращают JSON одного вида: `success` (получилось или нет), `message` (понятное описание) и, если есть что вернуть, `data`.

```json
{
  "success": true,
  "message": "Студент добавлен",
  "data": {
    "id": 1,
    "name": "Иван",
    "surname": "Иванов",
    "faculty": "ИТ",
    "age": 20
  }
}
```

```json
{
  "success": false,
  "message": "Студент с id=999999 не найден"
}
```

| Ситуация | HTTP | `success` | `message` (пример) |
|----------|------|-----------|--------------------|
| Данные получены, изменены или удалены | 200 | `true` | Найдено студентов: 2 |
| Запись добавлена (POST) | 201 | `true` | Студент добавлен |
| PUT без `id` | 400 | `false` | Для изменения студента укажите id |
| Некорректный JSON или `id` не число | 400 | `false` | Некорректное тело запроса: ожидается JSON с полями сущности |
| Записи с таким `id` нет | 404 | `false` | Студент с id=999999 не найден |
| Ошибка базы данных (например, слишком длинное имя) | 500 | `false` | Ошибка при работе с базой данных: … |

## Тестирование в Postman

В Postman нажмите `Import` и выберите `postman/MySpringBoot2Dbase.postman_collection.json`. В коллекции 21 запрос в двух папках («Студенты» и «Учебные дисциплины»): полный CRUD и неуспешные случаи. Выполняйте запросы сверху вниз: запрос на добавление сам сохраняет `id` новой записи в переменную коллекции, и остальные запросы используют её.

## Логирование

При запуске, а также при каждом CRUD-запросе в консоль выводятся сообщения уровня `INFO`. Пример после запуска:

```text
Подключение к базе данных H2 установлено: jdbc:h2:./testdb/students
В таблице STUDENTS записей: 0
В таблице SUBJECTS записей: 0
Приложение MySpringBoot2Dbase успешно запущено и готово принимать запросы
REST API студентов: http://localhost:8080/api/students
...
```

Пример для запроса `POST /api/students`:

```text
POST /api/students: добавление студента в базу данных: Student(id=null, name=Иван, surname=Иванов, faculty=ИТ, age=20)
Hibernate: insert into students (...) values (...)
StudentDAOImpl.saveStudent: запись сохранена в таблицу STUDENTS: Student(id=1, name=Иван, ...)
Ответ клиенту: HTTP 201, success=true, message='Студент добавлен'
```

## Загрузка на GitHub

Создайте на github.com **пустой** репозиторий `MySpringBoot2Dbase` (без README и `.gitignore`), затем в cmd или PowerShell:

```text
cd C:\Users\ВАШ_ПОЛЬЗОВАТЕЛЬ\IdeaProjects\MySpringBoot2Dbase
git init
git branch -M main
git add .
git status
git commit -m "Lab 6: REST CRUD service with H2 database"
git remote add origin https://github.com/ВАШ_ЛОГИН/MySpringBoot2Dbase.git
git push -u origin main
```

## Автор

Hammatov
