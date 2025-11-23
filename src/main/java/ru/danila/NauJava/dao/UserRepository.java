package ru.danila.NauJava.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danila.NauJava.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователями
 */
@Repository
public class UserRepository implements CrudRepository<User, Long> {

    private final List<User> m_userContainer;

    @Autowired
    public UserRepository(List<User> t_userContainer) {
        this.m_userContainer = t_userContainer;
    }

    @Override
    public void create(User t_user) {
        m_userContainer.add(t_user);
    }

    @Override
    public User read(Long t_id) {
        Optional<User> foundUser = m_userContainer.stream()
                .filter(user -> user.getId().equals(t_id))
                .findFirst();
        return foundUser.orElse(null);
    }

    @Override
    public void update(User t_user) {
        User existingUser = read(t_user.getId());
        if (existingUser != null) {
            existingUser.setUsername(t_user.getUsername());
            existingUser.setPassword(t_user.getPassword());
            existingUser.setFirstName(t_user.getFirstName());
            existingUser.setLastName(t_user.getLastName());
            existingUser.setRoles(t_user.getRoles());
        }
    }

    @Override
    public void delete(Long t_id) {
        User userToRemove = read(t_id);
        if (userToRemove != null) {
            m_userContainer.remove(userToRemove);
        }
    }

    /**
     * Поиск пользователя по имени
     */
    public User findByUsername(String t_username) {
        Optional<User> foundUser = m_userContainer.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(t_username))
                .findFirst();
        return foundUser.orElse(null);
    }

    public List<User> findAll() {
        return m_userContainer;
    }
}
