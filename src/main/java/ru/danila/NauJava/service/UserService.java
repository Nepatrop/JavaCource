package ru.danila.NauJava.service;

import ru.danila.NauJava.dto.UserRegistrationDTO;
import ru.danila.NauJava.entity.User;

import java.util.List;

/**
 * Интерфейс сервиса для работы с пользователями
 * Управляет регистрацией, аутентификацией и профилями пользователей
 */
public interface UserService {
    
    /**
     * Найти пользователя по username
     * @param t_username имя пользователя
     * @return пользователь
     */
    User findByUsername(String t_username);

    /**
     * Найти пользователя по email
     * @param t_email email пользователя
     * @return пользователь
     */
    User findByEmail(String t_email);

    /**
     * Найти пользователя по ID
     * @param t_userId ID пользователя
     * @return пользователь
     */
    User getUserById(Long t_userId);

    /**
     * Зарегистрировать нового пользователя
     * @param t_registrationDTO DTO с данными регистрации
     * @return созданный пользователь
     */
    User registerUser(UserRegistrationDTO t_registrationDTO);

    /**
     * Получить всех пользователей
     * @return список пользователей
     */
    List<User> getAllUsers();

    /**
     * Проверить существование пользователя по username
     * @param t_username имя пользователя
     * @return true если существует
     */
    boolean userExists(String t_username);

    /**
     * Проверить существование пользователя по email
     * @param t_email email
     * @return true если существует
     */
    boolean emailExists(String t_email);

    /**
     * Изменить пароль пользователя
     * @param t_userId ID пользователя
     * @param t_oldPassword старый пароль
     * @param t_newPassword новый пароль
     */
    void changePassword(Long t_userId, String t_oldPassword, String t_newPassword);

    /**
     * Активировать/деактивировать пользователя
     * @param t_userId ID пользователя
     * @param t_enabled статус активации
     */
    void setUserEnabled(Long t_userId, boolean t_enabled);

    /**
     * Проверить существование пользователя по username (альтернативный метод)
     * @param t_username имя пользователя
     * @return true если существует
     */
    boolean usernameExists(String t_username);

    /**
     * Сохранить пользователя
     * @param t_user пользователь
     * @return сохраненный пользователь
     */
    User saveUser(User t_user);
}
