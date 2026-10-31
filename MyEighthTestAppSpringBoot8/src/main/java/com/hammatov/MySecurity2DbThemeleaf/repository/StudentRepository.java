package com.hammatov.MySecurity2DbThemeleaf.repository;

import com.hammatov.MySecurity2DbThemeleaf.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
