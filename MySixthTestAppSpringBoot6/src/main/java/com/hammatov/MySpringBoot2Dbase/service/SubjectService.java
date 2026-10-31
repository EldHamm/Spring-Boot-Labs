package com.hammatov.MySpringBoot2Dbase.service;

import com.hammatov.MySpringBoot2Dbase.entity.Subject;

import java.util.List;

public interface SubjectService {

    List<Subject> getAllSubjects();

    Subject saveSubject(Subject subject);

    Subject getSubject(int id);

    boolean deleteSubject(int id);

}
