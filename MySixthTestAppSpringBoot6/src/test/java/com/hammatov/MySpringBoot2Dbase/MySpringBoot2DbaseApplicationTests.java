package com.hammatov.MySpringBoot2Dbase;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Тест использует базу в памяти, чтобы не занимать файл testdb/students.mv.db,
// который может быть открыт запущенным приложением
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
class MySpringBoot2DbaseApplicationTests {

    @Test
    void contextLoads() {
    }

}
