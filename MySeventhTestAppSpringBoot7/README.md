# MyUiRestDbService

Лабораторная работа №7 по Spring Boot: веб-приложение с пользовательским интерфейсом на Thymeleaf и Bootstrap 5, формами и базой данных H2. Приложение управляет двумя сущностями: студентами (`Student`) и учебными группами (`AcademicGroup`). Для каждой есть страница со списком (таблица DataTables) и форма добавления и редактирования. Каждое открытие страницы и каждая отправка формы пишут понятные сообщения в консоль через Lombok `@Slf4j`.

## Технологии

- Java 17
- Spring Boot 3.5 (Spring Web, Thymeleaf, Spring Data JPA, Actuator)
- H2 Database (встроенный режим, данные хранятся в файле)
- Bootstrap 5 и DataTables (подключаются с CDN, нужен доступ в интернет)
- Maven
- Lombok

## Структура проекта

```text
MyUiRestDbService
├── pom.xml
├── README.md
├── .gitignore
└── src
    ├── main
    │   ├── java/com/hammatov/MyUiRestDbService
    │   │   ├── MyUiRestDbServiceApplication.java
    │   │   ├── controller
    │   │   │   ├── AcademicGroupController.java
    │   │   │   └── StudentController.java
    │   │   ├── entity
    │   │   │   ├── AcademicGroup.java
    │   │   │   └── Student.java
    │   │   └── repository
    │   │       ├── AcademicGroupRepository.java
    │   │       └── StudentRepository.java
    │   └── resources
    │       ├── application.properties
    │       └── templates
    │           ├── add-group-form.html
    │           ├── add-student-form.html
    │           ├── fragments.html
    │           ├── list-groups.html
    │           └── list-students.html
    └── test/java/com/hammatov/MyUiRestDbService
        └── MyUiRestDbServiceApplicationTests.java
```

Папка `testdb` с файлом базы данных появится в корне проекта после первого запуска и не попадает в git.

## Запуск

Нужны JDK 17+ и Maven.

- в IntelliJ IDEA: `File → Open` → папка проекта → класс `MyUiRestDbServiceApplication` → Run;
- из терминала: `mvn spring-boot:run`.

Сервис стартует на порту **8080**. Запускайте приложение из корня проекта: путь к базе `jdbc:h2:./testdb/students` считается от рабочего каталога. Таблицы `STUDENTS` и `ACADEMIC_GROUPS` Hibernate создаёт сам (`spring.jpa.hibernate.ddl-auto=update`), вручную ничего создавать не нужно.

> Если IntelliJ IDEA не видит поле `log`, включите обработку аннотаций:
> `Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing`.

## Страницы и адреса

| Метод | Адрес | Что делает |
|-------|-------|-----------|
| GET | `/` или `/list` | Список студентов (`list-students.html`) |
| GET | `/addStudentForm` | Форма добавления студента (`add-student-form.html`) |
| POST | `/saveStudent` | Сохраняет студента (новый, если `id` пустой, иначе обновляет) и перенаправляет на `/list` |
| GET | `/showUpdateForm?id=1` | Форма редактирования студента с заполненными полями |
| GET | `/deleteStudent?id=1` | Удаляет студента и перенаправляет на `/list` |
| GET | `/groups` или `/listGroups` | Список учебных групп (`list-groups.html`) |
| GET | `/addGroupForm` | Форма добавления учебной группы (`add-group-form.html`) |
| POST | `/saveGroup` | Сохраняет группу и перенаправляет на `/groups` |
| GET | `/showGroupUpdateForm?id=1` | Форма редактирования группы |
| GET | `/deleteGroup?id=1` | Удаляет группу и перенаправляет на `/groups` |

Перенаправления реализованы через `RedirectView`, страницы возвращаются через `ModelAndView`. Если запись с переданным `id` не найдена, приложение пишет предупреждение в лог и возвращает на список. В таблицах работают поиск и сортировка DataTables (по столбцу с кнопками сортировка и поиск отключены).

Ограничения полей студента взяты из методички: имя до 15 символов, фамилия до 25, факультет до 20 (форма не даст ввести больше).

## Spring Boot Actuator

Actuator подключён в `pom.xml` и настроен в `application.properties`. Доступные адреса:

| Адрес | Что показывает |
|-------|----------------|
| `/actuator` | Список всех доступных endpoint'ов |
| `/actuator/health` | Состояние приложения и базы данных |
| `/actuator/info` | Название, описание и автор приложения |
| `/actuator/metrics` | Список метрик (например, `/actuator/metrics/jvm.memory.used`) |
| `/actuator/mappings` | Все адреса приложения и методы контроллеров |
| `/actuator/beans` | Все бины Spring |
| `/actuator/env` | Настройки и переменные окружения |
| `/actuator/loggers` | Уровни логирования |
| `/actuator/configprops` | Свойства конфигурации |

Так много endpoint'ов открыто для удобства учебной работы и скриншотов для отчёта. В реальных проектах их ограничивают и защищают.

## База данных H2

- Веб-консоль H2: <http://localhost:8080/h2-console>. Параметры входа: JDBC URL `jdbc:h2:./testdb/students`, пользователь `sa`, пароль пустой.
- SQL-запросы Hibernate выводятся в консоль приложения.
- Файл базы открыт приложением, поэтому DBeaver и другие внешние программы смогут подключиться к нему только после остановки приложения. Пока оно работает, пользуйтесь веб-консолью H2.

## Логирование

При запуске в консоль выводится состояние базы данных и адреса приложения:

```text
Подключение к базе данных H2 установлено: jdbc:h2:./testdb/students
В таблице STUDENTS записей: 0
В таблице ACADEMIC_GROUPS записей: 0
Приложение MyUiRestDbService успешно запущено и готово принимать запросы
Список студентов: http://localhost:8080/list
...
```

При открытии страниц и отправке форм:

```text
GET /list: открыта страница со списком студентов
Страница list-students: студентов в базе данных: 1
GET /addStudentForm: открыта форма добавления нового студента
POST /saveStudent: получена форма студента: Student(id=null, name=Иван, surname=Иванов, faculty=ИТ, age=20)
Студент добавлен: Student(id=1, name=Иван, ...)
Перенаправление на /list
```

## Загрузка на GitHub

Создайте на github.com **пустой** репозиторий `MyUiRestDbService` (без README и `.gitignore`), затем в cmd или PowerShell:

```text
cd C:\Users\ВАШ_ПОЛЬЗОВАТЕЛЬ\IdeaProjects\MyUiRestDbService
git init
git branch -M main
git add .
git status
git commit -m "Lab 7: Thymeleaf UI with students and academic groups"
git remote add origin https://github.com/ВАШ_ЛОГИН/MyUiRestDbService.git
git push -u origin main
```

## Автор

Hammatov
