package com.hammatov.MySecurity2DbThemeleaf.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Данные формы регистрации. toString не создаётся специально: в лог не должен попасть пароль
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotEmpty(message = "First name should not be empty")
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;

}
