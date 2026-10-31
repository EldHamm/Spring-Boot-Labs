package com.hammatov.MySpringBoot2Dbase.dao;

import com.hammatov.MySpringBoot2Dbase.entity.Student;

import java.util.List;

public interface StudentDAO {

    List<Student> getAllStudents();

    // Новую запись (id равен null) добавляет, существующую (id задан) обновляет
    Student saveStudent(Student student);

    // Возвращает null, если студента с таким id нет
    Student getStudent(int id);

    // Возвращает true, если запись была найдена и удалена
    boolean deleteStudent(int id);

}
