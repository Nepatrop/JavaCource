package ru.danila.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danila.NauJava.dao.UserRepository;
import ru.danila.NauJava.entity.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository m_userRepository;

    @Autowired
    public UserServiceImpl(UserRepository t_userRepository) {
        this.m_userRepository = t_userRepository;
    }

    @Override
    public User findByUsername(String t_username) {
        return m_userRepository.findByUsername(t_username);
    }

    @Override
    public void saveUser(User t_user) {
        m_userRepository.create(t_user);
    }

    @Override
    public List<User> getAllUsers() {
        return m_userRepository.findAll();
    }

    @Override
    public boolean userExists(String t_username) {
        return m_userRepository.findByUsername(t_username) != null;
    }
}
