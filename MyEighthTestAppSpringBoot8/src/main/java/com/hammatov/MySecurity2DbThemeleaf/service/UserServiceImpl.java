package com.hammatov.MySecurity2DbThemeleaf.service;

import com.hammatov.MySecurity2DbThemeleaf.dto.UserDto;
import com.hammatov.MySecurity2DbThemeleaf.entity.Role;
import com.hammatov.MySecurity2DbThemeleaf.entity.User;
import com.hammatov.MySecurity2DbThemeleaf.repository.RoleRepository;
import com.hammatov.MySecurity2DbThemeleaf.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    // Роль, которую получает каждый зарегистрированный пользователь
    private static final String DEFAULT_ROLE = "ROLE_ADMIN";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Одна транзакция: найденная роль остаётся привязанной к сессии и спокойно сохраняется вместе с пользователем
    @Override
    @Transactional
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        // В базу попадает только BCrypt-хеш, сам пароль нигде не сохраняется и в лог не пишется
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        log.info("UserServiceImpl: пароль пользователя {} закодирован алгоритмом BCrypt", user.getEmail());

        Role role = roleRepository.findByName(DEFAULT_ROLE);
        if (role == null) {
            log.info("UserServiceImpl: роль {} не найдена в базе данных, создаётся новая", DEFAULT_ROLE);
            role = createRole();
        }
        user.getRoles().add(role);
        log.info("UserServiceImpl: пользователю {} назначена роль {}", user.getEmail(), role.getName());

        User saved = userRepository.save(user);
        log.info("UserServiceImpl: пользователь сохранён в таблицу USERS: id={}, email={}", saved.getId(), saved.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    // Пароль в UserDto для списка пользователей не передаётся
    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        String[] nameParts = user.getName().split(" ", 2);
        userDto.setFirstName(nameParts[0]);
        userDto.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role createRole() {
        Role role = new Role();
        role.setName(DEFAULT_ROLE);
        return roleRepository.save(role);
    }

}
