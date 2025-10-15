package ru.danila.NauJava.dao;

/**
 * Обобщенный интерфейс для CRUD операций
 * T - тип сущности, ID - тип идентификатора
 */
public interface CrudRepository<T, ID> {
    // Базовые операции Create, Read, Update, Delete
    void create(T t_entity);
    T read(ID t_id);
    void update(T t_entity);
    void delete(ID t_id);
}