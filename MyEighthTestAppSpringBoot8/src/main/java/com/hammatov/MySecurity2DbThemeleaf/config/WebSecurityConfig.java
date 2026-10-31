package com.hammatov.MySecurity2DbThemeleaf.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // Кодировщик паролей. Метод static, чтобы бин создавался раньше остальных и не возникало циклических зависимостей
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Открыты всем: регистрация, главная страница, страница входа и страница ошибки
                        .requestMatchers("/register/**", "/index", "/", "/login", "/error").permitAll()
                        // hasRole("ADMIN") проверяет роль ROLE_ADMIN: префикс ROLE_ Spring Security добавляет сам
                        .requestMatchers("/users").hasRole("ADMIN")
                        // Все остальные страницы (в том числе студенты) только после входа
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/list")
                        .permitAll()
                )
                .logout(logout -> logout
                        // Выход по обычной ссылке /logout (метод GET), как в методичке
                        .logoutRequestMatcher(request -> "/logout".equals(request.getServletPath()))
                        .logoutSuccessUrl("/login?logout")
                        .addLogoutHandler((request, response, authentication) ->
                                log.info("Выход из системы: пользователь {}",
                                        authentication != null ? authentication.getName() : "неизвестен"))
                        .permitAll()
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.warn("Доступ запрещён: пользователь {} пытался открыть {}",
                                    request.getRemoteUser(), request.getRequestURI());
                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        })
                );
        return http.build();
    }

}
