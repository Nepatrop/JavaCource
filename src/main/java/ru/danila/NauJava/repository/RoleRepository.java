package ru.danila.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.danila.NauJava.entity.Role;

import java.util.Optional;

/**
 * JPA репозиторий для работы с сущностью Role
 * Обеспечивает CRUD операции и поиск ролей
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Найти роль по названию
     * @param t_name название роли
     * @return Optional с ролью
     */
    @Query("SELECT r FROM Role r WHERE UPPER(r.m_name) = UPPER(:name)")
    Optional<Role> findByM_name(@Param("name") String t_name);

    /**
     * Проверить существование роли
     * @param t_name название роли
     * @return true если существует
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Role r WHERE UPPER(r.m_name) = UPPER(:name)")
    boolean existsByM_name(@Param("name") String t_name);
}
