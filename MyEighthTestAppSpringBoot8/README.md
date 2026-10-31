# MySecurity2DbThemeleaf

Лабораторная работа №8 по Spring Boot: веб-приложение для управления студентами, защищённое Spring Security. Пользователи и роли хранятся в базе данных H2, пароли кодируются BCrypt, вход выполняется через форму `/login`, регистрация через `/register`. В консоль выводятся понятные сообщения при запуске, регистрации, успешном входе и ошибках авторизации (Lombok `@Slf4j`).

## Технологии

- Java 17
- Spring Boot 3.5 (Spring Web, Spring Security, Spring Data JPA, Validation, Thymeleaf)
- thymeleaf-extras-springsecurity6 (атрибуты `sec:authorize` и `sec:authentication` в шаблонах)
- H2 Database (встроенный режим, данные хранятся в файле)
- Bootstrap 5 и DataTables (подключаются с CDN, нужен доступ в интернет)
- Maven
- Lombok

## Структура проекта

```text
MySecurity2DbThemeleaf
├── pom.xml
├── README.md
├── .gitignore
└── src
    ├── main
    │   ├── java/com/hammatov/MySecurity2DbThemeleaf
    │   │   ├── MySecurity2DbThemeleafApplication.java
    │   │   ├── config
    │   │   │   ├── AuthenticationEventsListener.java
    │   │   │   └── WebSecurityConfig.java
    │   │   ├── controller
    │   │   │   ├── SecurityController.java
    │   │   │   └── StudentController.java
    │   │   ├── dto
    │   │   │   └── UserDto.java
    │   │   ├── entity
    │   │   │   ├── Role.java
    │   │   │   ├── Student.java
    │   │   │   └── User.java
    │   │   ├── repository
    │   │   │   ├── RoleRepository.java
    │   │   │   ├── StudentRepository.java
    │   │   │   └── UserRepository.java
    │   │   └── service
    │   │       ├── CustomUserDetailsService.java
    │   │       ├── UserService.java
    │   │       └── UserServiceImpl.java
    │   └── resources
    │       ├── application.properties
    │       └── templates
    │           ├── add-student-form.html
    │           ├── fragments.html
    │           ├── index.html
    │           ├── list-students.html
    │           ├── login.html
    │           ├── register.html
    │           └── users.html
    └── test/java/com/hammatov/MySecurity2DbThemeleaf
        ├── MySecurity2DbThemeleafApplicationTests.java
        └── SecurityRulesTests.java
```

Папка `testdb` с файлом базы данных появится в корне проекта после первого запуска и не попадает в git.

## Запуск

Нужны JDK 17+ и Maven.

- в IntelliJ IDEA: `File → Open` → папка проекта → класс `MySecurity2DbThemeleafApplication` → Run;
- из терминала: `mvn spring-boot:run`.

Сервис стартует на порту **8080**: <http://localhost:8080/index>. Запускайте приложение из корня проекта: путь к базе `jdbc:h2:./testdb/students` считается от рабочего каталога. Таблицы `users`, `roles`, `users_roles` и `students` Hibernate создаёт сам при первом запуске (`spring.jpa.hibernate.ddl-auto=update`), при следующих запусках данные сохраняются.

> Если IntelliJ IDEA не видит поле `log`, включите обработку аннотаций:
> `Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing`.

## Как работает защита

| Адрес | Кто имеет доступ |
|-------|------------------|
| `/`, `/index`, `/register/**`, `/login`, `/error` | Все, без входа |
| `/users` | Только пользователи с ролью `ROLE_ADMIN` |
| `/list`, `/addStudentForm`, `/saveStudent`, `/showUpdateForm`, `/deleteStudent` | Любой вошедший пользователь |
| `/logout` | Выход из системы, затем переход на `/login?logout` |

- Форма входа находится по адресу `/login`: логином служит email, паролем пароль, указанный при регистрации. После входа открывается список студентов `/list` (или страница, которую пользователь пытался открыть).
- `CustomUserDetailsService` загружает пользователя из таблицы `users` по email и передаёт Spring Security его BCrypt-хеш и роли.
- `UserServiceImpl.saveUser` кодирует пароль через `BCryptPasswordEncoder`, находит или создаёт роль `ROLE_ADMIN` и сохраняет пользователя. В таблице `users_roles` появляется связь «пользователь и роль».
- Защита от CSRF включена: формы Thymeleaf с `th:action` получают токен автоматически.
- Меню в шаблонах меняется по ситуации: гость видит Login и Register, вошедший пользователь видит Students и Logout, администратор ещё и Users.

> По заданию методички каждый новый пользователь получает роль `ROLE_ADMIN`, поэтому любой зарегистрировавшийся человек становится администратором. Для реального проекта по умолчанию выдают обычную роль (например, `ROLE_USER`), а администраторов назначают отдельно.

## Страницы

| Метод | Адрес | Что делает |
|-------|-------|-----------|
| GET | `/` или `/index` | Главная страница |
| GET | `/login` | Форма входа |
| GET | `/register` | Форма регистрации (имя, фамилия, email, пароль) |
| POST | `/register/save` | Проверяет форму, сохраняет пользователя и возвращает на `/register?success` |
| GET | `/users` | Список зарегистрированных пользователей (только ROLE_ADMIN) |
| GET | `/list` | Список студентов |
| GET | `/addStudentForm` | Форма добавления студента |
| POST | `/saveStudent` | Сохраняет студента и перенаправляет на `/list` |
| GET | `/showUpdateForm?id=1` | Форма редактирования студента |
| GET | `/deleteStudent?id=1` | Удаляет студента и перенаправляет на `/list` |

Правила проверки регистрации: имя, фамилия, email и пароль не должны быть пустыми, email должен быть корректным и ещё не зарегистрированным.

## Проверка работы

1. Откройте <http://localhost:8080/index>, затем Register и заполните форму: появится сообщение об успешной регистрации.
2. Откройте Login и введите email и пароль: откроется список студентов.
3. Нажмите Add Student, добавьте студента, затем проверьте кнопки Update и Delete.
4. Откройте Users: виден список зарегистрированных пользователей.
5. Нажмите Logout, затем попробуйте открыть <http://localhost:8080/list>: приложение вернёт на страницу входа.
6. Введите неверный пароль: на странице входа появится сообщение Invalid Email or Password.

Остановите приложение и откройте базу `testdb/students` в IntelliJ IDEA (вкладка Database) или в DBeaver: в таблицах `USERS`, `ROLES`, `USERS_ROLES` и `STUDENTS` видны созданные записи, а в столбце `PASSWORD` таблицы `USERS` лежит BCrypt-хеш (строка вида `$2a$10$…`), а не пароль. Пока приложение работает, файл базы открыт им, и внешние программы подключиться не смогут.

## Тесты

- в IntelliJ IDEA: правый клик по папке `src/test/java` → `Run 'All Tests'`;
- из терминала: `mvn test`.

`SecurityRulesTests` проверяет правила доступа: главная открыта всем, список студентов без входа ведёт на `/login`, `/users` закрыт для обычной роли и открыт для `ROLE_ADMIN`.

## Логирование

При запуске:

```text
Подключение к базе данных H2 установлено: jdbc:h2:./testdb/students
В таблице USERS записей: 0
В таблице ROLES записей: 0
В таблице STUDENTS записей: 0
Приложение MySecurity2DbThemeleaf успешно запущено и готово принимать запросы
...
```

При регистрации (пароль в лог не попадает):

```text
POST /register/save: получена форма регистрации, email=ivan@example.com
UserServiceImpl: пароль пользователя ivan@example.com закодирован алгоритмом BCrypt
UserServiceImpl: роль ROLE_ADMIN не найдена в базе данных, создаётся новая
UserServiceImpl: пользователю ivan@example.com назначена роль ROLE_ADMIN
UserServiceImpl: пользователь сохранён в таблицу USERS: id=1, email=ivan@example.com
Регистрация завершена: пользователь ivan@example.com добавлен в базу данных
```

При входе:

```text
CustomUserDetailsService: загрузка пользователя из базы данных по email ivan@example.com
CustomUserDetailsService: пользователь ivan@example.com найден, роли: [ROLE_ADMIN]
Успешный вход: пользователь ivan@example.com, роли [ROLE_ADMIN]
```

При неверном пароле или неизвестном email:

```text
Ошибка авторизации: вход пользователя 'ivan@example.com' отклонён (BadCredentialsException: Bad credentials)
```

## Загрузка на GitHub

Создайте на github.com **пустой** репозиторий `MySecurity2DbThemeleaf` (без README и `.gitignore`), затем в cmd или PowerShell:

```text
cd C:\Users\ВАШ_ПОЛЬЗОВАТЕЛЬ\IdeaProjects\MySecurity2DbThemeleaf
git init
git branch -M main
git add .
git status
git commit -m "Lab 8: Spring Security with users in database"
git remote add origin https://github.com/ВАШ_ЛОГИН/MySecurity2DbThemeleaf.git
git push -u origin main
```

## Автор

Hammatov
