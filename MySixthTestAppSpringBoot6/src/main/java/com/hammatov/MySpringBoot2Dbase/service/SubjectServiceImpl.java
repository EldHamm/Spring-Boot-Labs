package com.hammatov.MySpringBoot2Dbase.service;

import com.hammatov.MySpringBoot2Dbase.dao.SubjectDAO;
import com.hammatov.MySpringBoot2Dbase.entity.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

    private final SubjectDAO subjectDAO;

    @Autowired
    public SubjectServiceImpl(SubjectDAO subjectDAO) {
        this.subjectDAO = subjectDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getAllSubjects() {
        return subjectDAO.getAllSubjects();
    }

    @Override
    public Subject saveSubject(Subject subject) {
        return subjectDAO.saveSubject(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public Subject getSubject(int id) {
        return subjectDAO.getSubject(id);
    }

    @Override
    public boolean deleteSubject(int id) {
        return subjectDAO.deleteSubject(id);
    }

}
