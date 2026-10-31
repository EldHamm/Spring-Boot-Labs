package com.hammatov.MySecurity2DbThemeleaf;

import com.hammatov.MySecurity2DbThemeleaf.repository.RoleRepository;
import com.hammatov.MySecurity2DbThemeleaf.repository.StudentRepository;
import com.hammatov.MySecurity2DbThemeleaf.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class MySecurity2DbThemeleafApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySecurity2DbThemeleafApplication.class, args);

        // Эти сообщения выводятся после старта Tomcat и самого приложения
        log.info("Приложение MySecurity2DbThemeleaf успешно запущено и готово принимать запросы");
        log.info("Главная страница: http://localhost:8080/index");
        log.info("Регистрация: http://localhost:8080/register");
        log.info("Вход: http://localhost:8080/login");
        log.info("Spring Security: /register/** и /index открыты всем, /users только для роли ROLE_ADMIN, остальные страницы только после входа");
    }

    // Выполняется один раз при старте и сразу проверяет, что приложение умеет читать данные из базы
    @Bean
    CommandLineRunner logDatabaseState(UserRepository userRepository,
                                       RoleRepository roleRepository,
                                       StudentRepository studentRepository,
                                       @Value("${spring.datasource.url}") String datasourceUrl) {
        return args -> {
            log.info("Подключение к базе данных H2 установлено: {}", datasourceUrl);
            log.info("В таблице USERS записей: {}", userRepository.count());
            log.info("В таблице ROLES записей: {}", roleRepository.count());
            log.info("В таблице STUDENTS записей: {}", studentRepository.count());
        };
    }

}
