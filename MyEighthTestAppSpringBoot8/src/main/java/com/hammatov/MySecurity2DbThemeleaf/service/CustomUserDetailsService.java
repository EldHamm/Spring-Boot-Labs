package com.hammatov.MySecurity2DbThemeleaf.service;

import com.hammatov.MySecurity2DbThemeleaf.entity.Role;
import com.hammatov.MySecurity2DbThemeleaf.entity.User;
import com.hammatov.MySecurity2DbThemeleaf.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// Spring Security вызывает этот сервис при каждой попытке входа: он ищет пользователя в базе данных по email
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("CustomUserDetailsService: загрузка пользователя из базы данных по email {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("CustomUserDetailsService: пользователь с email {} не найден", email);
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        List<GrantedAuthority> authorities = mapRolesToAuthorities(user.getRoles());
        log.info("CustomUserDetailsService: пользователь {} найден, роли: {}", email, authorities);
        // Полное имя класса нужно, потому что наш класс User называется так же, как и класс Spring Security
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    private List<GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

}
