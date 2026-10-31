package com.hammatov.MyUiRestDbService.repository;

import com.hammatov.MyUiRestDbService.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

// Готовые методы findAll, findById, save, deleteById и другие даёт JpaRepository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
