package com.hammatov.MyUiRestDbService;

import com.hammatov.MyUiRestDbService.repository.AcademicGroupRepository;
import com.hammatov.MyUiRestDbService.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class MyUiRestDbServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyUiRestDbServiceApplication.class, args);

        // Эти сообщения выводятся после старта Tomcat и самого приложения
        log.info("Приложение MyUiRestDbService успешно запущено и готово принимать запросы");
        log.info("Список студентов: http://localhost:8080/list");
        log.info("Список учебных групп: http://localhost:8080/groups");
        log.info("Actuator: http://localhost:8080/actuator");
        log.info("Веб-консоль базы данных H2: http://localhost:8080/h2-console");
    }

    // Выполняется один раз при старте и сразу проверяет, что приложение умеет читать данные из базы
    @Bean
    CommandLineRunner logDatabaseState(StudentRepository studentRepository,
                                       AcademicGroupRepository academicGroupRepository,
                                       @Value("${spring.datasource.url}") String datasourceUrl) {
        return args -> {
            log.info("Подключение к базе данных H2 установлено: {}", datasourceUrl);
            log.info("В таблице STUDENTS записей: {}", studentRepository.count());
            log.info("В таблице ACADEMIC_GROUPS записей: {}", academicGroupRepository.count());
        };
    }

}
