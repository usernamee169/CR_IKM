package com.example.demo.repositories;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователями в базе данных.
 * Предоставляет стандартные CRUD-операции и специальные методы поиска для сущности {@link User}.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по имени пользователя (username).
     *
     * @param username имя пользователя для поиска (не может быть null или пустым)
     * @return {@link Optional} содержащий найденного пользователя или пустой, если пользователь не найден
     * @throws IllegalArgumentException если username равен null или пустой строке
     */
    Optional<User> findByUsername(@NonNull String username);

    /**
     * Проверяет существование пользователя по имени пользователя (username).
     *
     * @param username имя пользователя для проверки (не может быть null или пустым)
     * @return true если пользователь существует, false в противном случае
     * @throws IllegalArgumentException если username равен null или пустой строке
     */
    boolean existsByUsername(@NonNull String username);

    /**
     * Удаляет пользователя по имени пользователя (username).
     *
     * @param username имя пользователя для удаления (не может быть null или пустым)
     * @return количество удаленных записей
     * @throws IllegalArgumentException если username равен null или пустой строке
     */
    long deleteByUsername(@NonNull String username);
}