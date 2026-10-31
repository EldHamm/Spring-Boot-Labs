package com.hammatov.MySecurity2DbThemeleaf.controller;

import com.hammatov.MySecurity2DbThemeleaf.entity.Student;
import com.hammatov.MySecurity2DbThemeleaf.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class StudentController {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Страница со списком студентов: templates/list-students.html
    @GetMapping("/list")
    public ModelAndView getAllStudents() {
        log.info("GET /list: открыта страница со списком студентов");
        List<Student> students = studentRepository.findAll();
        log.info("Страница list-students: студентов в базе данных: {}", students.size());

        ModelAndView mav = new ModelAndView("list-students");
        mav.addObject("students", students);
        return mav;
    }

    // Форма добавления: templates/add-student-form.html с пустым студентом
    @GetMapping("/addStudentForm")
    public ModelAndView addStudentForm() {
        log.info("GET /addStudentForm: открыта форма добавления нового студента");
        ModelAndView mav = new ModelAndView("add-student-form");
        mav.addObject("student", new Student());
        return mav;
    }

    // Сохраняет нового студента (id пустой) или обновляет существующего (id заполнен)
    @PostMapping("/saveStudent")
    public RedirectView saveStudent(@ModelAttribute Student student) {
        log.info("POST /saveStudent: получена форма студента: {}", student);
        boolean isNew = student.getId() == null;
        Student saved = studentRepository.save(student);
        log.info("Студент {}: {}", isNew ? "добавлен" : "обновлён", saved);
        log.info("Перенаправление на /list");
        return new RedirectView("/list", true);
    }

    // Та же форма, но с данными студента, найденного по id
    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam("id") Long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            log.warn("GET /showUpdateForm?id={}: студент не найден, возврат к списку", id);
            return new ModelAndView(new RedirectView("/list", true));
        }
        log.info("GET /showUpdateForm?id={}: открыта форма редактирования студента: {}", id, optionalStudent.get());

        ModelAndView mav = new ModelAndView("add-student-form");
        mav.addObject("student", optionalStudent.get());
        return mav;
    }

    @GetMapping("/deleteStudent")
    public RedirectView deleteStudent(@RequestParam("id") Long id) {
        log.info("GET /deleteStudent?id={}: запрос на удаление студента", id);
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            log.info("Студент с id={} удалён из базы данных", id);
        } else {
            log.warn("Студент с id={} не найден, удалять нечего", id);
        }
        log.info("Перенаправление на /list");
        return new RedirectView("/list", true);
    }

}
