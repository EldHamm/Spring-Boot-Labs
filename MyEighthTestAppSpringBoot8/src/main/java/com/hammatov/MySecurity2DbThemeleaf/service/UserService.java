package com.hammatov.MySecurity2DbThemeleaf.service;

import com.hammatov.MySecurity2DbThemeleaf.dto.UserDto;
import com.hammatov.MySecurity2DbThemeleaf.entity.User;

import java.util.List;

public interface UserService {

    // Создаёт пользователя из формы регистрации: кодирует пароль и назначает роль по умолчанию
    void saveUser(UserDto userDto);

    // Возвращает null, если пользователя с таким email нет
    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

}
