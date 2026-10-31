package com.hammatov.MySecurity2DbThemeleaf.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

// Spring Security сам публикует события входа, а этот класс записывает их в консоль
@Slf4j
@Component
public class AuthenticationEventsListener {

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        log.info("Успешный вход: пользователь {}, роли {}", authentication.getName(), authentication.getAuthorities());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        log.warn("Ошибка авторизации: вход пользователя '{}' отклонён ({}: {})",
                event.getAuthentication().getName(),
                event.getException().getClass().getSimpleName(),
                event.getException().getMessage());
    }

}
