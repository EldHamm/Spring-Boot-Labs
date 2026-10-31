package com.hammatov.MySpringBoot2Dbase.dao;

import com.hammatov.MySpringBoot2Dbase.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class StudentDAOImpl implements StudentDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = entityManager.createQuery("from Student", Student.class).getResultList();
        log.info("StudentDAOImpl.getAllStudents: из таблицы STUDENTS получено записей: {}", students.size());
        return students;
    }

    @Override
    public Student saveStudent(Student student) {
        // merge сам выбирает действие: INSERT для новой записи и UPDATE для существующей
        Student saved = entityManager.merge(student);
        log.info("StudentDAOImpl.saveStudent: запись сохранена в таблицу STUDENTS: {}", saved);
        return saved;
    }

    @Override
    public Student getStudent(int id) {
        Student student = entityManager.find(Student.class, id);
        if (student == null) {
            log.info("StudentDAOImpl.getStudent: запись с id={} в таблице STUDENTS не найдена", id);
        } else {
            log.info("StudentDAOImpl.getStudent: запись с id={} найдена: {}", id, student);
        }
        return student;
    }

    @Override
    public boolean deleteStudent(int id) {
        Query query = entityManager.createQuery("delete from Student where id = :studentId");
        query.setParameter("studentId", id);
        int deleted = query.executeUpdate();
        log.info("StudentDAOImpl.deleteStudent: удалено записей с id={}: {}", id, deleted);
        return deleted > 0;
    }

}
