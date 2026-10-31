package com.hammatov.MySpringBoot2Dbase.dao;

import com.hammatov.MySpringBoot2Dbase.entity.Subject;

import java.util.List;

public interface SubjectDAO {

    List<Subject> getAllSubjects();

    // Новую запись (id равен null) добавляет, существующую (id задан) обновляет
    Subject saveSubject(Subject subject);

    // Возвращает null, если дисциплины с таким id нет
    Subject getSubject(int id);

    // Возвращает true, если запись была найдена и удалена
    boolean deleteSubject(int id);

}
