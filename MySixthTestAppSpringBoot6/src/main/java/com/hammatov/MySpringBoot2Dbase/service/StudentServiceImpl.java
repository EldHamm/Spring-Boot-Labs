package com.hammatov.MySpringBoot2Dbase.service;

import com.hammatov.MySpringBoot2Dbase.dao.StudentDAO;
import com.hammatov.MySpringBoot2Dbase.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO;

    @Autowired
    public StudentServiceImpl(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    @Override
    public Student saveStudent(Student student) {
        return studentDAO.saveStudent(student);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudent(int id) {
        return studentDAO.getStudent(id);
    }

    @Override
    public boolean deleteStudent(int id) {
        return studentDAO.deleteStudent(id);
    }

}
