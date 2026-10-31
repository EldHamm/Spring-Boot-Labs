package com.hammatov.MySpringBoot2Dbase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Учебная дисциплина
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SUBJECTS")
public class Subject {

    /** Идентификатор дисциплины: база данных назначает его сама */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Название дисциплины, до 50 символов */
    @Column(name = "name", length = 50)
    private String name;

    /** Описание дисциплины, до 255 символов */
    @Column(name = "description")
    private String description;

}
