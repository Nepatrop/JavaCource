package ru.danila.NauJava.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.danila.NauJava.entity.User;
import ru.danila.NauJava.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Сервис для загрузки пользователей Spring Security
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService m_userService;

    @Autowired
    public CustomUserDetailsService(UserService t_userService) {
        this.m_userService = t_userService;
    }

    @Override
    public UserDetails loadUserByUsername(String t_username) throws UsernameNotFoundException {
        User appUser = m_userService.findByUsername(t_username);

        if (appUser == null) {
            throw new UsernameNotFoundException("Пользователь не найден: " + t_username);
        }

        // Преобразуем роли в GrantedAuthority
        Collection<GrantedAuthority> authorities = appUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                appUser.getUsername(),
                appUser.getPassword(),
                authorities
        );
    }
}
