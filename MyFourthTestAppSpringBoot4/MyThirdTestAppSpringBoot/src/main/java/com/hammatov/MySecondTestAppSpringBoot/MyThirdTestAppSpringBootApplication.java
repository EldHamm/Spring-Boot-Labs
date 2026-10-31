package com.hammatov.MySecondTestAppSpringBoot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MyThirdTestAppSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyThirdTestAppSpringBootApplication.class, args);

        // Эти сообщения выводятся после старта Tomcat и самого приложения
        log.info("Сервис 2 (MyThirdTestAppSpringBoot) успешно запущен и готов принимать запросы");
        log.info("Принимает request от Сервиса 1: POST http://localhost:8084/feedback");
    }

}
