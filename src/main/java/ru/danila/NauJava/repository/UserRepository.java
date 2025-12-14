package ru.danila.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.danila.NauJava.entity.User;

import java.util.Optional;

/**
 * JPA репозиторий для работы с сущностью User
 * Обеспечивает CRUD операции и поиск пользователей
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Найти пользователя по имени пользователя (username)
     * @param t_username имя пользователя
     * @return Optional с пользователем
     */
    @Query("SELECT u FROM User u WHERE UPPER(u.m_username) = UPPER(:username)")
    Optional<User> findByM_usernameIgnoreCase(@Param("username") String t_username);

    /**
     * Найти пользователя по электронной почте
     * @param t_email email
     * @return Optional с пользователем
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.m_roles WHERE UPPER(u.m_email) = UPPER(:email)")
    Optional<User> findByM_emailIgnoreCase(@Param("email") String t_email);

    /**
     * Проверить существование пользователя по username
     * @param t_username username
     * @return true если существует
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE UPPER(u.m_username) = UPPER(:username)")
    boolean existsByM_usernameIgnoreCase(@Param("username") String t_username);

    /**
     * Проверить существование пользователя по email
     * @param t_email email
     * @return true если существует
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE UPPER(u.m_email) = UPPER(:email)")
    boolean existsByM_emailIgnoreCase(@Param("email") String t_email);

    /**
     * Найти активного пользователя по username
     * @param t_username username
     * @return Optional с пользователем
     */
    @Query("SELECT u FROM User u WHERE UPPER(u.m_username) = UPPER(:username) AND u.m_isEnabled = true")
    Optional<User> findActiveByUsername(@Param("username") String t_username);

    /**
     * Найти пользователя по связанному сотруднику
     * @param t_employeeId ID сотрудника
     * @return Optional с пользователем
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.m_roles WHERE u.m_employee.m_id = :employeeId")
    Optional<User> findByEmployeeId(@Param("employeeId") Long t_employeeId);
}
