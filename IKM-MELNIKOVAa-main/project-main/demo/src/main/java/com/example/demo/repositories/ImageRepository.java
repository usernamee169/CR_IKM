package com.example.demo.repositories;

import com.example.demo.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с изображениями в базе данных.
 * Предоставляет стандартные CRUD-операции для сущности {@link Image}.
 * Наследует базовые методы работы с JPA из {@link JpaRepository}.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}