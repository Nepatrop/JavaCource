package ru.danila.NauJava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danila.NauJava.dto.UserRegistrationDTO;
import ru.danila.NauJava.entity.Role;
import ru.danila.NauJava.entity.User;
import ru.danila.NauJava.exception.ResourceNotFoundException;
import ru.danila.NauJava.repository.RoleRepository;
import ru.danila.NauJava.repository.UserRepository;

import java.util.List;

/**
 * Реализация сервиса для работы с пользователями
 * Управляет регистрацией, аутентификацией и профилями
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository m_userRepository;
    private final RoleRepository m_roleRepository;
    private final PasswordEncoder m_passwordEncoder;

    /**
     * Конструктор с внедрением зависимостей
     */
    @Autowired
    public UserServiceImpl(UserRepository t_userRepository,
                         RoleRepository t_roleRepository,
                         PasswordEncoder t_passwordEncoder) {
        this.m_userRepository = t_userRepository;
        this.m_roleRepository = t_roleRepository;
        this.m_passwordEncoder = t_passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String t_username) {
        return m_userRepository.findByM_usernameIgnoreCase(t_username).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String t_email) {
        return m_userRepository.findByM_emailIgnoreCase(t_email).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long t_userId) {
        return m_userRepository.findById(t_userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + t_userId + " не найден"));
    }

    @Override
    public User registerUser(UserRegistrationDTO t_registrationDTO) {
        // Проверка, что пароли совпадают
        if (!t_registrationDTO.getM_password().equals(t_registrationDTO.getM_confirmPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }

        // Проверка, не существует ли пользователь с таким username
        if (m_userRepository.existsByM_usernameIgnoreCase(t_registrationDTO.getM_username())) {
            throw new IllegalArgumentException("Пользователь с таким username уже существует");
        }

        // Проверка, не существует ли пользователь с таким email
        if (m_userRepository.existsByM_emailIgnoreCase(t_registrationDTO.getM_email())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        // Создать нового пользователя
        User s_user = new User();
        s_user.setM_username(t_registrationDTO.getM_username());
        s_user.setM_email(t_registrationDTO.getM_email());
        s_user.setM_password(m_passwordEncoder.encode(t_registrationDTO.getM_password()));
        s_user.setM_isEnabled(true);

        // Добавить роль USER по умолчанию
        Role s_userRole = m_roleRepository.findByM_name("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Роль ROLE_USER не найдена"));
        s_user.addRole(s_userRole);

        User s_result = m_userRepository.save(s_user);
        return s_result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return m_userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(String t_username) {
        return m_userRepository.existsByM_usernameIgnoreCase(t_username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String t_email) {
        return m_userRepository.existsByM_emailIgnoreCase(t_email);
    }

    @Override
    public void changePassword(Long t_userId, String t_oldPassword, String t_newPassword) {

        User s_user = getUserById(t_userId);

        // Проверить старый пароль
        if (!m_passwordEncoder.matches(t_oldPassword, s_user.getM_password())) {
            throw new IllegalArgumentException("Неверный старый пароль");
        }

        // Установить новый пароль
        s_user.setM_password(m_passwordEncoder.encode(t_newPassword));
        m_userRepository.save(s_user);
    }

    @Override
    public void setUserEnabled(Long t_userId, boolean t_enabled) {
        User s_user = getUserById(t_userId);
        s_user.setM_isEnabled(t_enabled);
        m_userRepository.save(s_user);
    }

    @Override
    public boolean usernameExists(String t_username) {
        return m_userRepository.existsByM_usernameIgnoreCase(t_username);
    }

    @Override
    public User saveUser(User t_user) {
        return m_userRepository.save(t_user);
    }
}
