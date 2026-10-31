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
        log.info("Приложение MySecondTestAppSpringBoot успешно запущено и готово принимать запросы");
        log.info("REST-сервис доступен по адресу: POST http://localhost:8082/feedback");
    }

}
