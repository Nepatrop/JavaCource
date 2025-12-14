package ru.danila.NauJava.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danila.NauJava.entity.User;
import ru.danila.NauJava.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Сервис для загрузки пользователей Spring Security
 */
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService m_userService;

    @Autowired
    public CustomUserDetailsService(UserService t_userService) {
        this.m_userService = t_userService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String t_email) throws UsernameNotFoundException {
        // Ищем пользователя по email (вход в систему по почте)
        User appUser = m_userService.findByEmail(t_email);

        if (appUser == null) {
            throw new UsernameNotFoundException("Пользователь не найден: " + t_email);
        }

        // Преобразуем роли в GrantedAuthority (добавляем ROLE_ только если его нет)
        Collection<GrantedAuthority> authorities = appUser.getRoles().stream()
                .map(role -> {
                    String roleName = role.getName();
                    if (!roleName.startsWith("ROLE_")) {
                        roleName = "ROLE_" + roleName;
                    }
                    return new SimpleGrantedAuthority(roleName);
                })
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                appUser.getEmail(),  // Используем email как principal
                appUser.getPassword(),
                authorities
        );
    }
}
