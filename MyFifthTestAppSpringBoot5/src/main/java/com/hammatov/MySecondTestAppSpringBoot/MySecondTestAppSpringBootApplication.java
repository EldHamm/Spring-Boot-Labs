package com.hammatov.MySecondTestAppSpringBoot;

import com.hammatov.MySecondTestAppSpringBoot.model.Positions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Year;

@Slf4j
@SpringBootApplication
public class MySecondTestAppSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySecondTestAppSpringBootApplication.class, args);

        // Эти сообщения выводятся после старта Tomcat и самого приложения
        log.info("Сервис 1 (MySecondTestAppSpringBoot) успешно запущен и готов принимать запросы");
        log.info("Принимает: POST http://localhost:8082/feedback, считает премии и пересылает request на Сервис 2: POST http://localhost:8084/feedback");

        Year year = Year.now();
        log.info("Текущий год {}: {} дней ({}), это число используется в формуле годовой премии",
                year, year.length(), year.isLeap() ? "високосный" : "не високосный");

        log.info("Должности для расчёта премий:");
        for (Positions position : Positions.values()) {
            log.info("  {}: коэффициент {}, {}", position, position.getPositionCoefficient(),
                    position.isManager() ? "руководитель (считается квартальная премия)" : "не руководитель");
        }
    }

}
