package com.hammatov.MySecurity2DbThemeleaf.controller;

import com.hammatov.MySecurity2DbThemeleaf.dto.UserDto;
import com.hammatov.MySecurity2DbThemeleaf.entity.User;
import com.hammatov.MySecurity2DbThemeleaf.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
public class SecurityController {

    private final UserService userService;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    // Главная страница: templates/index.html
    @GetMapping({"/", "/index"})
    public String home() {
        log.info("GET /index: открыта главная страница");
        return "index";
    }

    // Форма входа: templates/login.html. Саму проверку логина и пароля выполняет Spring Security
    @GetMapping("/login")
    public String login() {
        log.info("GET /login: открыта страница входа");
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        log.info("GET /register: открыта форма регистрации");
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result) {
        log.info("POST /register/save: получена форма регистрации, email={}", userDto.getEmail());

        String email = userDto.getEmail();
        if (email != null && !email.isBlank()) {
            User existingUser = userService.findUserByEmail(email);
            if (existingUser != null) {
                log.warn("Регистрация отклонена: email {} уже зарегистрирован", email);
                result.rejectValue("email", null, "An account with this email already exists");
            }
        }

        if (result.hasErrors()) {
            log.warn("Регистрация отклонена: ошибок в форме: {}", result.getErrorCount());
            result.getFieldErrors().forEach(fieldError ->
                    log.warn("Поле '{}': {}", fieldError.getField(), fieldError.getDefaultMessage()));
            return "register";
        }

        userService.saveUser(userDto);
        log.info("Регистрация завершена: пользователь {} добавлен в базу данных", email);
        return "redirect:/register?success";
    }

    // Список пользователей: доступен только роли ROLE_ADMIN (правило в WebSecurityConfig)
    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> users = userService.findAllUsers();
        log.info("GET /users: открыт список зарегистрированных пользователей, всего: {}", users.size());
        model.addAttribute("users", users);
        return "users";
    }

}
