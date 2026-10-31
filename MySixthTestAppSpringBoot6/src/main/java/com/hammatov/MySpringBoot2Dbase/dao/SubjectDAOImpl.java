package com.hammatov.MySpringBoot2Dbase.dao;

import com.hammatov.MySpringBoot2Dbase.entity.Subject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class SubjectDAOImpl implements SubjectDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = entityManager.createQuery("from Subject", Subject.class).getResultList();
        log.info("SubjectDAOImpl.getAllSubjects: из таблицы SUBJECTS получено записей: {}", subjects.size());
        return subjects;
    }

    @Override
    public Subject saveSubject(Subject subject) {
        // merge сам выбирает действие: INSERT для новой записи и UPDATE для существующей
        Subject saved = entityManager.merge(subject);
        log.info("SubjectDAOImpl.saveSubject: запись сохранена в таблицу SUBJECTS: {}", saved);
        return saved;
    }

    @Override
    public Subject getSubject(int id) {
        Subject subject = entityManager.find(Subject.class, id);
        if (subject == null) {
            log.info("SubjectDAOImpl.getSubject: запись с id={} в таблице SUBJECTS не найдена", id);
        } else {
            log.info("SubjectDAOImpl.getSubject: запись с id={} найдена: {}", id, subject);
        }
        return subject;
    }

    @Override
    public boolean deleteSubject(int id) {
        Query query = entityManager.createQuery("delete from Subject where id = :subjectId");
        query.setParameter("subjectId", id);
        int deleted = query.executeUpdate();
        log.info("SubjectDAOImpl.deleteSubject: удалено записей с id={}: {}", id, deleted);
        return deleted > 0;
    }

}
