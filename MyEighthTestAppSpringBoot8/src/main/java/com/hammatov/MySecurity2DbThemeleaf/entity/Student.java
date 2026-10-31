package com.hammatov.MySecurity2DbThemeleaf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "STUDENTS")
public class Student {

    /** Идентификатор студента: база данных назначает его сама */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Имя студента, до 15 символов */
    @Column(name = "name", length = 15)
    private String name;

    /** Фамилия студента, до 25 символов */
    @Column(name = "surname", length = 25)
    private String surname;

    /** Факультет, до 20 символов */
    @Column(name = "faculty", length = 20)
    private String faculty;

    /** Возраст студента */
    @Column(name = "age")
    private Integer age;

}
