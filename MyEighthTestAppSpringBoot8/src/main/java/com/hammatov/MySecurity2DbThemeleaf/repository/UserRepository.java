package com.hammatov.MySecurity2DbThemeleaf.repository;

import com.hammatov.MySecurity2DbThemeleaf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Возвращает null, если пользователя с таким email нет
    User findByEmail(String email);

}
