package com.hammatov.MySpringBoot2Dbase;

import com.hammatov.MySpringBoot2Dbase.service.StudentService;
import com.hammatov.MySpringBoot2Dbase.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class MySpringBoot2DbaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySpringBoot2DbaseApplication.class, args);

        // Эти сообщения выводятся после старта Tomcat и самого приложения
        log.info("Приложение MySpringBoot2Dbase успешно запущено и готово принимать запросы");
        log.info("REST API студентов: http://localhost:8080/api/students");
        log.info("REST API дисциплин: http://localhost:8080/api/subjects");
        log.info("Веб-консоль базы данных H2: http://localhost:8080/h2-console");
    }

    // Выполняется один раз при старте и сразу проверяет, что приложение умеет читать данные из базы
    @Bean
    CommandLineRunner logDatabaseState(StudentService studentService,
                                       SubjectService subjectService,
                                       @Value("${spring.datasource.url}") String datasourceUrl) {
        return args -> {
            log.info("Подключение к базе данных H2 установлено: {}", datasourceUrl);
            log.info("В таблице STUDENTS записей: {}", studentService.getAllStudents().size());
            log.info("В таблице SUBJECTS записей: {}", subjectService.getAllSubjects().size());
        };
    }

}
