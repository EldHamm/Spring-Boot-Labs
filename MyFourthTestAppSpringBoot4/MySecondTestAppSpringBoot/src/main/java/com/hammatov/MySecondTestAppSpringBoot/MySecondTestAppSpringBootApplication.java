package com.hammatov.MySecondTestAppSpringBoot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MySecondTestAppSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySecondTestAppSpringBootApplication.class, args);

        // Эти сообщения выводятся после старта Tomcat и самого приложения
        log.info("Сервис 1 (MySecondTestAppSpringBoot) успешно запущен и готов принимать запросы");
        log.info("Принимает: POST http://localhost:8082/feedback, пересылает request на Сервис 2: POST http://localhost:8084/feedback");
    }

}
