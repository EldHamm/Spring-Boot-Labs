package com.hammatov.MyUiRestDbService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Учебная группа
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACADEMIC_GROUPS")
public class AcademicGroup {

    /** Идентификатор группы: база данных назначает его сама */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название группы, до 20 символов */
    @Column(name = "name", length = 20)
    private String name;

    /** Количество студентов в группе (состав группы) */
    @Column(name = "component_count")
    private Integer componentCount;

}
