package com.hammatov.MySecurity2DbThemeleaf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    /** Идентификатор пользователя */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Имя и фамилия пользователя через пробел */
    @Column(nullable = false)
    private String name;

    /** Email, он же логин при входе; уникальный */
    @Column(nullable = false, unique = true)
    private String email;

    /** Пароль в виде BCrypt-хеша (исходный пароль нигде не хранится) */
    @Column(nullable = false)
    private String password;

    /**
     * Роли пользователя. Связь «многие ко многим» хранится в таблице users_roles.
     * Роли загружаются сразу (EAGER), потому что они нужны Spring Security при входе.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles = new ArrayList<>();

}
