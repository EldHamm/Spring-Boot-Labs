package com.hammatov.MySpringBoot2Dbase.controller;

import com.hammatov.MySpringBoot2Dbase.dto.ApiResponse;
import com.hammatov.MySpringBoot2Dbase.entity.Student;
import com.hammatov.MySpringBoot2Dbase.entity.Subject;
import com.hammatov.MySpringBoot2Dbase.service.StudentService;
import com.hammatov.MySpringBoot2Dbase.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class MyController {

    private final StudentService studentService;
    private final SubjectService subjectService;

    @Autowired
    public MyController(StudentService studentService, SubjectService subjectService) {
        this.studentService = studentService;
        this.subjectService = subjectService;
    }

    // ==================== Студенты ====================

    @GetMapping("/students")
    public ResponseEntity<ApiResponse> allStudents() {
        log.info("GET /api/students: запрос списка всех студентов из базы данных");
        List<Student> students = studentService.getAllStudents();
        return respond(HttpStatus.OK, ApiResponse.ok("Найдено студентов: " + students.size(), students));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse> getStudent(@PathVariable("id") int id) {
        log.info("GET /api/students/{}: запрос одного студента из базы данных", id);
        Student student = studentService.getStudent(id);
        if (student == null) {
            return respond(HttpStatus.NOT_FOUND, ApiResponse.failure("Студент с id=" + id + " не найден"));
        }
        return respond(HttpStatus.OK, ApiResponse.ok("Студент найден", student));
    }

    @PostMapping("/students")
    public ResponseEntity<ApiResponse> saveStudent(@RequestBody Student student) {
        log.info("POST /api/students: добавление студента в базу данных: {}", student);
        student.setId(null); // новая запись: идентификатор назначает база данных
        Student saved = studentService.saveStudent(student);
        return respond(HttpStatus.CREATED, ApiResponse.ok("Студент добавлен", saved));
    }

    @PutMapping("/students")
    public ResponseEntity<ApiResponse> updateStudent(@RequestBody Student student) {
        log.info("PUT /api/students: изменение студента в базе данных: {}", student);
        if (student.getId() == null) {
            return respond(HttpStatus.BAD_REQUEST, ApiResponse.failure("Для изменения студента укажите id"));
        }
        if (studentService.getStudent(student.getId()) == null) {
            return respond(HttpStatus.NOT_FOUND, ApiResponse.failure("Студент с id=" + student.getId() + " не найден"));
        }
        return respond(HttpStatus.OK, ApiResponse.ok("Студент изменён", studentService.saveStudent(student)));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable("id") int id) {
        log.info("DELETE /api/students/{}: удаление студента из базы данных", id);
        if (!studentService.deleteStudent(id)) {
            return respond(HttpStatus.NOT_FOUND, ApiResponse.failure("Студент с id=" + id + " не найден"));
        }
        return respond(HttpStatus.OK, ApiResponse.ok("Студент с id=" + id + " удалён", null));
    }

    // ==================== Учебные дисциплины ====================

    @GetMapping("/subjects")
    public ResponseEntity<ApiResponse> allSubjects() {
        log.info("GET /api/subjects: запрос списка всех дисциплин из базы данных");
        List<Subject> subjects = subjectService.getAllSubjects();
        return respond(HttpStatus.OK, ApiResponse.ok("Найдено дисциплин: " + subjects.size(), subjects));
    }

    @GetMapping("/subjects/{id}")
    public ResponseEntity<ApiResponse> getSubject(@PathVariable("id") int id) {
        log.info("GET /api/subjects/{}: запрос одной дисциплины из базы данных", id);
        Subject subject = subjectService.getSubject(id);
        if (subject == null) {
            return respond(HttpStatus.NOT_FOUND, ApiResponse.failure("Дисциплина с id=" + id + " не найдена"));
        }
        return respond(HttpStatus.OK, ApiResponse.ok("Дисциплина найдена", subject));
    }

    @PostMapping("/subjects")
    public ResponseEntity<ApiResponse> saveSubject(@RequestBody Subject subject) {
        log.info("POST /api/subjects: добавление дисциплины в базу данных: {}", subject);
        subject.setId(null); // новая запись: идентификатор назначает база данных
        Subject saved = subjectService.saveSubject(subject);
        return respond(HttpStatus.CREATED, ApiResponse.ok("Дисциплина добавлена", saved));
    }

    @PutMapping("/subjects")
    public ResponseEntity<ApiResponse> updateSubject(@RequestBody Subject subject) {
        log.info("PUT /api/subjects: изменение дисциплины в базе данных: {}", subject);
        if (subject.getId() == null) {
            return respond(HttpStatus.BAD_REQUEST, ApiResponse.failure("Для изменения дисциплины укажите id"));
        }
        if (subjectService.getSubject(subject.getId()) == null) {
            return respond(HttpStatus.NOT_FOUND, ApiResponse.failure("Дисциплина с id=" + subject.getId() + " не найдена"));
        }
        return respond(HttpStatus.OK, ApiResponse.ok("Дисциплина изменена", subjectService.saveSubject(subject)));
    }

    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<ApiResponse> deleteSubject(@PathVariable("id") int id) {
        log.info("DELETE /api/subjects/{}: удаление дисциплины из базы данных", id);
        if (!subjectService.deleteSubject(id)) {
            return respond(HttpStatus.NOT_FOUND, ApiResponse.failure("Дисциплина с id=" + id + " не найдена"));
        }
        return respond(HttpStatus.OK, ApiResponse.ok("Дисциплина с id=" + id + " удалена", null));
    }

    // ==================== Ошибки ====================
    // Любая ошибка тоже превращается в JSON того же формата: success = false и понятное сообщение

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse> handleDatabaseError(DataAccessException e) {
        log.error("Ошибка при работе с базой данных: {}", e.getMostSpecificCause().getMessage(), e);
        return respond(HttpStatus.INTERNAL_SERVER_ERROR,
                ApiResponse.failure("Ошибка при работе с базой данных: " + e.getMostSpecificCause().getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleBadJson(HttpMessageNotReadableException e) {
        log.error("Некорректное тело запроса: {}", e.getMessage());
        return respond(HttpStatus.BAD_REQUEST,
                ApiResponse.failure("Некорректное тело запроса: ожидается JSON с полями сущности"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleBadParameter(MethodArgumentTypeMismatchException e) {
        log.error("Некорректное значение параметра '{}': {}", e.getName(), e.getValue());
        return respond(HttpStatus.BAD_REQUEST,
                ApiResponse.failure("Некорректное значение параметра '" + e.getName() + "': " + e.getValue()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnexpectedError(Exception e) {
        log.error("Непредвиденная ошибка: {}", e.getMessage(), e);
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, ApiResponse.failure("Непредвиденная ошибка: " + e.getMessage()));
    }

    // Все ответы проходят через один метод, поэтому итог каждой операции попадает в лог
    private ResponseEntity<ApiResponse> respond(HttpStatus status, ApiResponse body) {
        log.info("Ответ клиенту: HTTP {}, success={}, message='{}'", status.value(), body.isSuccess(), body.getMessage());
        return ResponseEntity.status(status).body(body);
    }

}
