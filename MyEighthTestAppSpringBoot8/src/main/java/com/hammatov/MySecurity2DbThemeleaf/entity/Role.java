package com.hammatov.MySecurity2DbThemeleaf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Роль пользователя, например ROLE_ADMIN.
// Вместо @Data здесь @Getter и @Setter: у связанных сущностей User и Role общий toString/hashCode зациклился бы
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    /** Идентификатор роли */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название роли, уникальное: ROLE_ADMIN */
    @Column(nullable = false, unique = true)
    private String name;

    /** Пользователи с этой ролью (обратная сторона связи, таблицей управляет User) */
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

}
