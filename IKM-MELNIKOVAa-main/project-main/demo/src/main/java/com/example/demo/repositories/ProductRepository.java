package com.example.demo.repositories;

import com.example.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Репозиторий для работы с продуктами в базе данных.
 * Предоставляет стандартные CRUD-операции и специальные запросы для сущности {@link Product}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Находит продукты, содержащие указанную строку в названии (без учета регистра).
     *
     * @param title часть названия продукта для поиска (не может быть null)
     * @return список продуктов, удовлетворяющих условию поиска (может быть пустым)
     * @throws IllegalArgumentException если title равен null
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Product> findByTitle(@NonNull String title);

    /**
     * Находит все продукты, отсортированные по дате создания (новые сначала).
     *
     * @return список всех продуктов, отсортированный по дате создания
     */
    @Query("SELECT p FROM Product p ORDER BY p.dateOfCreated DESC")
    List<Product> findAllOrderByDateDesc();

    /**
     * Проверяет существование продукта по его идентификатору.
     *
     * @param id идентификатор продукта (не может быть null)
     * @return true если продукт существует, false в противном случае
     * @throws IllegalArgumentException если id равен null
     */
    @Override
    boolean existsById(@NonNull Long id);

    /**
     * Находит продукт по его идентификатору.
     *
     * @param id идентификатор продукта (не может быть null)
     * @return найденный продукт или null если не найден
     * @throws IllegalArgumentException если id равен null
     */
    @Override
    Product getById(@NonNull Long id);
}