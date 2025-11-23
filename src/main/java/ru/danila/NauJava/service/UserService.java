package ru.danila.NauJava.service;

import ru.danila.NauJava.entity.User;

import java.util.List;

/**
 * Сервис для работы с пользователями
 */
public interface UserService {
    User findByUsername(String t_username);
    void saveUser(User t_user);
    List<User> getAllUsers();
    boolean userExists(String t_username);
}
