package com.hammatov.MyFirstTestAppSpringBoot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MyFirstTestAppSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyFirstTestAppSpringBootApplication.class, args);

        // Эти сообщения выводятся после старта Tomcat и самого приложения
        log.info("Приложение MyFirstTestAppSpringBoot успешно запущено и готово принимать запросы");
        log.info("Адрес приложения: http://localhost:8080");
        log.info("Доступные GET-запросы: /hello, /update-array?s=текст, /show-array, "
                + "/update-map?s=текст, /show-map, /show-all-lenght");
    }

}
