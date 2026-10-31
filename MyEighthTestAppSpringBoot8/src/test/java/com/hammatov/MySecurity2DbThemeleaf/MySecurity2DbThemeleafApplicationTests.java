package com.hammatov.MySecurity2DbThemeleaf;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Тест использует базу в памяти, чтобы не занимать файл testdb/students.mv.db,
// который может быть открыт запущенным приложением
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
class MySecurity2DbThemeleafApplicationTests {

    @Test
    void contextLoads() {
    }

}
